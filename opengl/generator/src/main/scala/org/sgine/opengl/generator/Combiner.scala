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
import arg.VariableArgument
import java.lang.reflect.{Method, Field}

import com.googlecode.reflective._

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Combiner(methodNames: List[String], e1: ClassExtractor, e2: ClassExtractor) {
  var unusedAndroid: List[Method] = Nil
  var unusedLWJGL: List[Method] = Nil
  var noMatches: List[String] = Nil

  val fields = combineFields(e1.fields, Nil)
  val methods = combineMethods(methodNames)

  @tailrec
  private def combineFields(fields: List[Field], combined: List[CombinedField]): List[CombinedField] = {
    if (fields.length > 0) {
      val field1 = fields.head
      val list = e2.fields(field1.getName) match {
        case Some(field2) => {
          val value1 = field1.get(null)
          val value2 = field2.get(null)
          if (value1 != value2) throw new RuntimeException("Different values for field: " + field1.getName + " - " + value1 + " / " + value2)

          CombinedField(field1.getName, field1.getType, value1, field1, field2) :: combined
        }
        case None => combined
      }

      combineFields(fields.tail, list)
    } else {
      combined
    }
  }

  @tailrec
  private def combineMethods(methodNames: List[String], combined: List[CombinedMethod] = Nil): List[CombinedMethod] = {
    if (methodNames.length > 0) {
      val methodName = methodNames.head
      val combinedMethods = generateCombined(methodName)
      if (combinedMethods.isEmpty) {
        noMatches = methodName :: noMatches
      }
      combineMethods(methodNames.tail, combinedMethods ::: combined)
    } else {
      combined
    }
  }

  var e1Methods: List[Method] = Nil
  var e2Methods: List[Method] = Nil
  private def generateCombined(name: String): List[CombinedMethod] = {
    e1Methods = e1.methods(name)
    e2Methods = e2.methods(name)

    val isPerfect = MethodMatcher.isPerfectMatch(_: String, _: Method, _: Method, false)
    val isAlmostPerfect = MethodMatcher.isPerfectMatch(_: String, _: Method, _: Method, true)
    val matchers = List(isPerfect, isAlmostPerfect)
    val combined = runMatchers(name, matchers, Nil)

    unusedAndroid = e1Methods ::: unusedAndroid
    unusedLWJGL = e2Methods ::: unusedLWJGL

    combined
  }

  type MatchTest = (String, Method, Method) => Option[CombinedMethod]

  @tailrec
  private def runMatchers(name: String, matchers: List[MatchTest], combined: List[CombinedMethod]): List[CombinedMethod] = {
    if (matchers.length == 0) {
      combined
    } else {
      var cms = combined
      val matcher = matchers.head
      for (m1 <- e1Methods) {
        firstMatch(name, m1, matcher, e2Methods) match {
          case Some(cm) => {
            e1Methods = e1Methods.filterNot(m => cm.descriptor.androidMethodCreator.methods.contains(m))
            e2Methods = e2Methods.filterNot(m => cm.descriptor.lwjglMethodCreator.methods.contains(m))
            cms = cm :: cms
          }
          case None => // No match
        }
      }

      runMatchers(name, matchers.tail, cms)
    }
  }

  @tailrec
  private def firstMatch(name: String, m1: Method, matcher: MatchTest, methods: List[Method]): Option[CombinedMethod] = {
    if (methods.length == 0) {
      None
    } else {
      val m2 = methods.head
      matcher(name, m1, m2) match {
        case Some(cm) => Some(cm)
        case None => firstMatch(name, m1, matcher, methods.tail)
      }
    }
  }
}