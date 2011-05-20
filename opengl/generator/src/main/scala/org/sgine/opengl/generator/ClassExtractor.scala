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
import java.lang.reflect.{Field, Method}
import java.lang.reflect.Modifier._

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ClassExtractor(static: Boolean) {
  private var _fields: List[Field] = Nil
  private var _methods: List[Method] = Nil

  var classes: List[Class[_]] = Nil
  def fields = _fields
  def methods = _methods

  def process() = {
    recurseClasses(classes)

    // Reverse the list
    _fields = _fields.sortWith((a, b) => a.getName.compareTo(b.getName) > 0)
    _methods = _methods.sortWith((a, b) => a.getName.compareTo(b.getName) > 0)
  }

  def fields(name: String) = _fields.find(_.getName == name)

  def methods(name: String) = _methods.filter {
    method => {
      method.getName.startsWith(name) && (method.getName.length - name.length) <= 2
    }
  }

  def methodNames() = _methods.foldLeft[List[String]](Nil)((list, method) => if (list.contains(method.getName)) list else method.getName :: list)

  @tailrec
  private def recurseClasses(classes: List[Class[_]]): Unit = {
    if (classes.length > 0) {
      val c = classes.head

      // Populate fields and methods
      populateFields(c.getDeclaredFields.toList)
      populateMethods(c.getDeclaredMethods.toList)

      recurseClasses(classes.tail)
    }
  }

  @tailrec
  private def populateFields(fields: List[Field]): Unit = {
    if (fields.length > 0) {
      val f = fields.head
      if (isStatic(f.getModifiers) && isPublic(f.getModifiers) && (_fields.find(field => field.getName == f.getName) == None)) {
        _fields = f :: _fields
      }

      populateFields(fields.tail)
    }
  }

  @tailrec
  private def populateMethods(methods: List[Method]): Unit = {
    if (methods.length > 0) {
      val m = methods.head
      if (isStatic(m.getModifiers) == static && isPublic(m.getModifiers) && isValidMethod(m)) {
        _methods = m :: _methods
      }

      populateMethods(methods.tail)
    }
  }

  private def isValidMethod(m: Method) = {
    val name = m.getDeclaringClass.getName + "." + m.getName
    if (name.endsWith("glGetTexParameterxv")) {
      false
    } else if (name.endsWith("glTexParameterxv")) {
      false
    } else if (name == "org.lwjgl.opengl.GL41.glClearDepthf") {
      false
    } else {
      true
    }
  }
}