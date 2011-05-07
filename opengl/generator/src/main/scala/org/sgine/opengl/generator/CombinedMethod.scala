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

import java.lang.reflect.Method

import ClassCreator._

import com.googlecode.reflective.EnhancedClass._

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class CombinedMethod(methodName: String, descriptor: MethodDescriptor, matcher: String) {
  def generateAbstract() = {
    val b = new StringBuilder()
    // TODO: populate scaladoc
    generateSignature(b)
    b.toString()
  }

  def generate() = {
    val b = new StringBuilder()
    generateSignature(b)
    b.append(" = {\r\n")
    b.append("\t\tinstance.")
    b.append(methodName)
    b.append('(')
    b.append(descriptor.args.map(t => fix(t._1)).mkString(", "))
    b.append(')')
    b.append("\r\n\t}")
    b.toString()
  }

  def generateAndroid() = {
    val b = new StringBuilder()
    generateSignature(b)
    generateBody(b, descriptor.androidBody)
    b.toString()
  }

  def generateLWJGL() = {
    val b = new StringBuilder()
    generateSignature(b)
    generateBody(b, descriptor.lwjglBody)
    b.toString()
  }

  private def generateSignature(b: StringBuilder) = {
    b.append("\tdef ")
    b.append(methodName)
    b.append('(')
    b.append(descriptor.args.map(t => fix(t._1) + ": " + convertClass(t._2)).mkString(", "))
    b.append("): ")
    b.append(convertClass(descriptor.returnType))
  }

  private def generateBody(b: StringBuilder, body: String) = {
    b.append(" = {\r\n")
    b.append("\t\t")
    b.append(body.replaceAll("\r\n", "\r\n\t\t"))
    b.append("\r\n\t}")
  }
}