/*
 * Copyright (c) 2011 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.opengl.generator

import arg.{Argument, ConversionArgument, ExplicitArgument, VariableArgument}
import java.lang.reflect.Method

import com.googlecode.reflective._
import util.matching.Regex

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 5/6/11
 */
object MethodMatcher {
  def perfectMatch(name: String, m1: Method, m2: Method, m1s: List[Method], m2s: List[Method], almost: Boolean) = {
    if ((m1.getName == m2.getName || almost) && parametersMatch(m1, m2) && m1.getReturnType == m2.getReturnType) {
      val argNames = m1.args.map(a => a.name).toList
      // TODO: refactor ConversionArgument to support left and right along with changing the MethodDescriptor arg types
      val args = argNames.map(arg => VariableArgument(arg))
//      val argsRight = for ((arg1, arg2) <- m1.args zip m2.args) yield {
//        val c1 = arg1.`type`.clazz
//        val c2 = arg2.`type`.clazz
//        val arg = arg1.name
//        if (c1 == c2) {
//          VariableArgument(arg)
//        } else if (isNumeric(c1) && isNumeric(c2)) {
//          VariableArgument(arg)
//        } else if (ConversionArgument.has(c1, c2)) {
//          ConversionArgument(arg, c1, c2)
//        } else {
//          throw new RuntimeException("UNHANDLED SUPPORT FOR: " + arg1 + " / " + arg2)
//        }
//      }
      val androidMethodCreator = DynamicMethodCreator(m1, args)
      val lwjglMethodCreator = DynamicMethodCreator(m2, args)
      val descriptor = MethodDescriptor(m1.getName, argNames.zip(m1.getParameterTypes), m1.getReturnType, androidMethodCreator, lwjglMethodCreator)
      val matcher = if (almost) "Almost Perfect" else "Perfect"
      Some(CombinedMethod(m1.getName, descriptor, matcher))
    } else {
      None
    }
  }

  def argMatch(name: String, m1: Method, m2: Method, m1s: List[Method], m2s: List[Method]) = {
    if (m1.getParameterTypes.length == m2.getParameterTypes.length && m1.args.zip(m2.args).forall((t) => t._1.name == t._2.name)) {
      println("ARG MATCH FOUND: " + m1 + " - " + m2)
      None
    } else {
      None
    }
  }

  /*def signaturePointerMatch(name: String, m1: Method, m2: Method, m1s: List[Method], m2s: List[Method]) = {
    val androidSignature = "size: Int, type: Int, stride: Int, pointer: java.nio.Buffer"
    val lwjglSignature = "size: Int, stride: Int, pointer: java.nio.FloatBuffer"
    val lwjglRegex = "size: Int, stride: Int, pointer: java[.]nio[.].*Buffer"
    // TODO: support DoubleBuffer, IntBuffer, and ShortBuffer
    if (argsString(m1) == androidSignature && argsString(m2) == lwjglSignature) {
      val args = List("size" -> classOf[Int], "stride" -> classOf[Int], "pointer" -> classOf[java.nio.Buffer])
      val amc = DynamicMethodCreator(m1, List(VariableArgument("size"), ExplicitArgument("0"), VariableArgument("pointer")))
      val lmc = DynamicMethodCreator(m2, List(VariableArgument("size"), ExplicitArgument("0"), ConversionArgument("pointer", classOf[java.nio.Buffer], classOf[java.nio.FloatBuffer])))
      val lwjglMethods = m2s.collect {
        case m if (argsString(m).matches(lwjglRegex)) => m
      }
//      val lmc2 = new MethodCreator {
//
//      }
      println("METHOD MATCHES (" + name + "): " + lwjglMethods.length + " of " + m2s.length)
      val descriptor = MethodDescriptor(m1.getName, args, m1.getReturnType, amc, lmc)
      Some(CombinedMethod(m1.getName, descriptor, "Signature"))
    } else {
      None
    }
  }*/

  def smartMatch(name: String, m1: Method, m2: Method, m1s: List[Method], m2s: List[Method]) = {
    val me1 = method2EnhancedMethod(m1)
    val debug = OpenGLGenerator.debugMethodName == name
    if (debug) println("SMART MATCH: " + name + " - " + method2EnhancedMethod(m1))
    var matches: List[SmartMatch] = Nil
    for (m2 <- m2s) {
      if (debug) println("  " + m2)
      val me2 = method2EnhancedMethod(m2)
      val argumentsRight = new Array[Argument](me2.args.length)
      var noMatch = false
      var conversion: MethodArgument = null
      for (index <- 0 until argumentsRight.length) {
        val ma2 = me2.args(index)
        me1.arg(ma2.name) match {
          case Some(ma1) => {
            if (debug) println("    Argument Match: " + ma1)
            val v = if (!isBadMatch(ma1.`type`.clazz, ma2.`type`.clazz)) {
              VariableArgument(ma1.name)
            } else {
              if (conversion != null) throw new RuntimeException("More than one conversion found!")
              conversion = ma2
              VariableArgument("conversion")
            }
            argumentsRight(index) = v
          }
          case None => {
            if (debug) println("    No Match: " + me2.args(index).name)
            noMatch = true
          }
        }
      }
      if (!noMatch) {
        if (debug) println("  FULL MATCH: " + me1 + " - " + me2)
        matches = SmartMatch(m2, conversion, argumentsRight.toList) :: matches
      }
    }
    if (matches.isEmpty) {
      None
    } else if (matches.length == 1) {
      if (debug) println("    Only one match: " + method2EnhancedMethod(matches.head.method))
      None
    } else if (matches.head.conversion == null) {
      if (debug) println("    No conversion for: " + method2EnhancedMethod(matches.head.method))
      None
    } else {
      // Create LWJGL method body
      val b = new StringBuilder()
      b.append(matches.head.conversion.name)
      b.append(" match {\r\n")
      for (m <- matches) {
        b.append("\tcase conversion: ")
        b.append(m.conversion.`type`.name)
        b.append(" => ")
        b.append(m.method.getDeclaringClass.getName)
        b.append('.')
        b.append(m.method.getName)
        b.append('(')
        b.append(m.argsRight.map(ma => ma.string).mkString(", "))
        b.append(")\r\n")
      }
      b.append("\tcase _ => error(\"Failed conversion!\")\r\n")
      b.append('}')

      val argNames = m1.args.map(a => a.name)
      val args = argNames.map(arg => VariableArgument(arg))
      val androidMethodCreator = DynamicMethodCreator(m1, args)
      val lwjglMethodCreator = StaticMethodCreator(matches.map(sm => sm.method), b.toString)
      val descriptor = MethodDescriptor(m1.getName, argNames.zip(m1.getParameterTypes), m1.getReturnType, androidMethodCreator, lwjglMethodCreator)
      val matcher = "Smart"
      Some(CombinedMethod(m1.getName, descriptor, matcher))
    }
  }

  case class SmartMatch(method: Method, conversion: MethodArgument, argsRight: List[Argument])

  private def argsString(m: EnhancedMethod) = {
    val s = m.toString
    s.substring(s.indexOf('(') + 1, s.lastIndexOf(')'))
  }

  private def parametersMatch(left: Method, right: Method) = {
    if (left.getParameterTypes.length == right.getParameterTypes.length) {
      left.getParameterTypes.zip(right.getParameterTypes).find(isBadMatch) match {
        case Some(b) => false
        case None => true
      }
    } else {
      false
    }
  }

  private def isBadMatch(t: (Class[_], Class[_])) = {
    if (t._1 != t._2) {
      if (isNumeric(t._1) && isNumeric(t._2)) {
        false
        // TODO re-enable when ConversionArgument is supported in perfectMatch
//      } else if (ConversionArgument.has(t._1, t._2)) {
//        false
      } else {
        true
      }
    } else {
      false
    }
  }

  private def isNumeric(c: Class[_]) = c.getName match {
    case "byte" => true
    case "short" => true
    case "int" => true
    case "long" => true
    case "float" => true
    case "double" => true
    case n => false
  }
}