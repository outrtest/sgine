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

import annotation.tailrec
import io.Source
import java.lang.reflect.{Modifier, Method}

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
class CombinedMethods(val name: String, left: List[Method], right: List[Method]) {
  private var androidDocReflection = Map.empty[Class[_], AndroidDocReflection]

  private var _methods: List[CombinedMethod] = Nil
  def methods = _methods

  private var workLeft: List[Method] = left
  private var workRight: List[Method] = right

  generateMethods()

  private def generateMethods(): Unit = {
    findTemplate()
    findPerfect()

    println(name + " - Methods: " + methods.length + ", Left: " + left.length + " (" + workLeft.length + "), Right: " + right.length + " (" + workRight.length + ")")
    if (methods.length == 0) {
//      println(name + ": Unable to find match: " + left.length + " - " + right.length)
//      println("\tLeft:")
//      left.foreach(m => println("\t\t" + m + " - " + adr(m.getDeclaringClass).methodArgs(m.getName)))
//      println("\tRight:")
//      right.foreach(m => println("\t\t" + m))
//      System.exit(0)
    } else {
//      println("Matches found for: " + name + " - " + methods.length + ", Left over: " + workLeft.length + ", " + workRight.length)
//      for (m <- methods) {
//        println(m.left.code)
//        println(m.right.code)
//      }
    }
  }

  private def findTemplate() = {
    if (hasTemplate(name)) {
      val androidMethod = template(name + ".android.template")
      val lwjglMethod = template(name + ".lwjgl.template")
      val leftSig = MethodSignature(androidMethod, workLeft: _*)
      val rightSig = MethodSignature(lwjglMethod, workRight: _*)
      val argNames = if (left.head.getParameterTypes.length > 0) {
        adr(left.head.getDeclaringClass).methodArgs(left.head.getName)
      } else {
        Nil
      }

      _methods = CombinedMethod(name, left.head.getParameterTypes, argNames, left.head.getReturnType, leftSig, rightSig) :: _methods
      workLeft = Nil
      workRight = Nil
    }
  }

  private def findPerfect() = {
    for (l <- workLeft; r <- workRight; if (l.getName == r.getName); if (parametersMatch(l, r))) {
      val argNames = if (l.getParameterTypes.length > 0) {
        adr(l.getDeclaringClass).methodArgs(l.getName)
      } else {
        Nil
      }
      val left = MethodSignature(createPerfectMethod(l, argNames), l)
      val right = MethodSignature(createPerfectMethod(r, argNames), r)
      _methods = CombinedMethod(name, l.getParameterTypes, argNames, l.getReturnType, left, right) :: _methods
      workLeft = workLeft.filterNot(_ == l)
      workRight = workRight.filterNot(_ == r)
    }
  }

  private def createPerfectMethod(method: Method, args: List[String]) = {
    val b = new StringBuilder()

    if (args.length != method.getParameterTypes.length) {
      throw new RuntimeException("Invalid param name count for: " + method + " arg names: " + args.length + " - " + name)
    }
    b.append("\tdef ")
    b.append(name)
    b.append("(")
    for ((argType, index) <- method.getParameterTypes.zipWithIndex) {
      if (index > 0) {
        b.append(", ")
      }
      b.append(args(index))
      b.append(": ")
      b.append(Generator.convertClass(argType))
    }
    b.append("): ")
    b.append(Generator.convertClass(method.getReturnType))
    b.append(" = {\r\n")
    b.append("\t\t")
    if (Modifier.isStatic(method.getModifiers)) {
      b.append(method.getDeclaringClass.getName)
    } else {
      b.append("instance")
    }
    b.append('.')
    b.append(method.getName)
    b.append("(")
    for ((argType, index) <- method.getParameterTypes.zipWithIndex) {
      if (index > 0) {
        b.append(", ")
      }
      b.append(args(index))
    }
    b.append(")")
    b.append("\r\n")
    b.append("\t}")

    b.toString
  }

  private def parametersMatch(left: Method, right: Method) = {
    if (left.getParameterTypes.length == right.getParameterTypes.length) {
      left.getParameterTypes.zip(right.getParameterTypes).find(t => t._1 != t._2) match {
        case Some(b) => false
        case None => true
      }
    } else {
      false
    }
  }

  private def hasTemplate(name: String) = getClass.getClassLoader.getResource(name + ".android.template") != null

  private def template(name: String) = Source.fromURL(getClass.getClassLoader.getResource(name)).mkString

  private def adr(c: Class[_]) = androidDocReflection.get(c) match {
    case Some(ad) => ad
    case None => {
      val ad = new AndroidDocReflection(c.getName)
      androidDocReflection += c -> ad
      ad
    }
  }
}



