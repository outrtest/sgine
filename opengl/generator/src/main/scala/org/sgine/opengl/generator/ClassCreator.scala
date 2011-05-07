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

import io.Source
import java.io.File
import annotation.tailrec

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 5/2/11
 */
class ClassCreator(combiner: Combiner) {
  private val templateAPI = fromTemplate("gl.api.template")
  private val templateAndroid = fromTemplate("gl.android.template")
  private val templateLWJGL = fromTemplate("gl.lwjgl.template")

  def generateAPI() = {
    var s = templateAPI

    // $abstractMethods
    val abstractMethods = generate(combiner.methods, new StringBuilder(), true, (m: CombinedMethod) => m.generateAbstract())
    s = s.replaceAll("[$]abstractMethods", abstractMethods)

    // $fields
    val fields = generate(combiner.fields, new StringBuilder(), true, (f: CombinedField) => f.generate())
    s = s.replaceAll("[$]fields", fields)

    // $methods
    val methods = generate(combiner.methods, new StringBuilder(), true, (m: CombinedMethod) => m.generate())
    s = s.replaceAll("[$]methods", methods)

    s
  }

  def generateAndroid() = {
    var s = templateAndroid

    // $methods
    val methods = generate(combiner.methods, new StringBuilder(), true, (m: CombinedMethod) => m.generateAndroid)
    s = s.replaceAll("[$]methods", methods)

    s
  }

  def generateLWJGL() = {
    var s = templateLWJGL

    // $methods
    val methods = generate(combiner.methods, new StringBuilder(), true, (m: CombinedMethod) => m.generateLWJGL)
    s = s.replaceAll("[$]methods", methods)

    s
  }

  @tailrec
  private def generate[T](list: List[T], b: StringBuilder, first: Boolean, f: T => String): String = {
    if (list.length == 0) {
      b.toString
    } else {
      val t = list.head
      if (!first) b.append("\r\n\r\n")
      b.append(f(t))

      generate(list.tail, b, false, f)
    }
  }

  private def fromTemplate(name: String) = Source.fromURL(getClass.getClassLoader.getResource(name)).mkString
}

object ClassCreator {
  def toDoc(s: String) = {
    "\t/**\r\n\t * " + s.replaceAll("\r\n", "\r\n\t * ") + "\r\n\t */\r\n"
  }

  def fix(argName: String) = if (argName == "type") {
    "`type`"
  } else {
    argName
  }
}