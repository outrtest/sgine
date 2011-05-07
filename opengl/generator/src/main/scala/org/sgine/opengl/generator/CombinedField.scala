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

import java.lang.reflect.Field

import com.googlecode.reflective.EnhancedClass._

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class CombinedField(name: String, fieldType: Class[_], value: Any, leftOrigin: Field, rightOrigin: Field) {
  def generate() = {
    val b = new StringBuilder()
    b.append(ClassCreator.toDoc(createDocText))
    b.append("\tval ")
    b.append(name)
    b.append(": ")
    b.append(convertClass(fieldType))
    b.append(" = ")
    b.append(value)
    b.toString()
  }

  private def createDocText() = {
    val b = new StringBuilder()
    b.append("Constant Value: ")
    b.append(value)
    b.append("\r\n")
    b.append("\r\n")
    b.append("@see ")
    b.append(leftOrigin.getDeclaringClass.getName)
    b.append('#')
    b.append(leftOrigin.getName)
    b.append("\r\n")
    b.append("@see ")
    b.append(rightOrigin.getDeclaringClass.getName)
    b.append('#')
    b.append(rightOrigin.getName)
    b.toString()
  }
}