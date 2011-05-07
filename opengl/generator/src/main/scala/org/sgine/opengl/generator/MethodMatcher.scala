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

import arg.VariableArgument
import java.lang.reflect.Method

import com.googlecode.reflective._

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 5/6/11
 */
object MethodMatcher {
  def isPerfectMatch(name: String, m1: Method, m2: Method, almost: Boolean) = {
    if ((m1.getName == m2.getName || almost) && parametersMatch(m1, m2) && m1.getReturnType == m2.getReturnType) {
      val argNames = m1.args.map(a => a.name)
      val args = argNames.map(arg => VariableArgument(arg))
      val androidMethodCreator = DynamicMethodCreator(m1, args)
      val lwjglMethodCreator = DynamicMethodCreator(m2, args)
      val descriptor = MethodDescriptor(m1.getName, argNames.zip(m1.getParameterTypes), m1.getReturnType, androidMethodCreator, lwjglMethodCreator)
      val matcher = if (almost) "Almost Perfect" else "Perfect"
      Some(CombinedMethod(m1.getName, descriptor, matcher))
    } else {
      None
    }
  }

  def isArgMatch(name: String, m1: Method, m2: Method) = {
    if (m1.getParameterTypes.length == m2.getParameterTypes.length && m1.args.zip(m2.args).forall((t) => t._1.name == t._2.name)) {
      println("ARG MATCH FOUND: " + m1 + " - " + m2)
      None
    } else {
      None
    }
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