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

package org.sgine.opengl.generator.explicit

import java.lang.reflect.Method
import org.sgine.{EnumEntry, Enumerated}
import com.googlecode.reflective._
import org.sgine.opengl.generator.{StaticMethodCreator, MethodDescriptor, CombinedMethod}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed trait ExplicitMatcher extends EnumEntry {
  def methodName: String
  def args: List[(String, Class[_])]
  def returnType: Class[_]
  def left: List[EnhancedMethod]
  def right: List[EnhancedMethod]
  def androidBody: String
  def lwjglBody: String

  def isMatch(androidMethod: Method) = left.find(em => em.javaMethod == androidMethod) != None

  def toCombinedMethod = {
    val ac = StaticMethodCreator(left.map(em => em.javaMethod), androidBody)
    val lc = StaticMethodCreator(right.map(em => em.javaMethod), lwjglBody)
    val descriptor = MethodDescriptor(methodName, args, returnType, ac, lc)
    CombinedMethod(methodName, descriptor, "Explicit")
  }
}

object ExplicitMatcher extends Enumerated[ExplicitMatcher] {
  case object glVertexPointer extends ExplicitMatcher {
    val methodName = "glVertexPointer"
    val args = List(
      "size" -> classOf[Int],
      "type" -> classOf[Int],
      "stride" -> classOf[Int],
      "pointer" -> classOf[java.nio.Buffer]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES10.glVertexPointer(size: Int, type: Int, stride: Int, pointer: java.nio.Buffer): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL11.glVertexPointer(size: Int, stride: Int, pointer: java.nio.ShortBuffer): Unit").get,
      method("org.lwjgl.opengl.GL11.glVertexPointer(size: Int, stride: Int, pointer: java.nio.IntBuffer): Unit").get,
      method("org.lwjgl.opengl.GL11.glVertexPointer(size: Int, stride: Int, pointer: java.nio.FloatBuffer): Unit").get,
      method("org.lwjgl.opengl.GL11.glVertexPointer(size: Int, stride: Int, pointer: java.nio.DoubleBuffer): Unit").get
    )
    val androidBody = """android.opengl.GLES10.glVertexPointer(size, `type`, stride, pointer)"""
    val lwjglBody = """pointer match {
      | case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL11.glVertexPointer(size, stride, b)
      | case b: java.nio.IntBuffer => org.lwjgl.opengl.GL11.glVertexPointer(size, stride, b)
      | case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL11.glVertexPointer(size, stride, b)
      | case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL11.glVertexPointer(size, stride, b)
      | case _ => error("Failed conversion of buffer type: " + pointer.getClass.getName)
      |}""".stripMargin
  }

  def find(androidMethod: Method): Option[ExplicitMatcher] = ExplicitMatcher.find(em => em.isMatch(androidMethod))

  val values = List(glVertexPointer)
}