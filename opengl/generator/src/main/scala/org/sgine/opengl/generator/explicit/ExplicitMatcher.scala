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
import java.nio.IntBuffer

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
  val glColorPointer = new ExplicitMatcher {
    val methodName = "glColorPointer"
    val args = List(
      "size" -> classOf[Int],
      "type" -> classOf[Int],
      "stride" -> classOf[Int],
      "pointer" -> classOf[java.nio.Buffer]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES10.glColorPointer(size: Int, type: Int, stride: Int, pointer: java.nio.Buffer): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL11.glColorPointer(size: Int, stride: Int, pointer: java.nio.FloatBuffer): Unit").get,
      method("org.lwjgl.opengl.GL11.glColorPointer(size: Int, stride: Int, pointer: java.nio.DoubleBuffer): Unit").get
    )
    val androidBody = """android.opengl.GLES10.glColorPointer(size, `type`, stride, pointer)"""
    val lwjglBody = """pointer match {
      | case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL11.glColorPointer(size, stride, b)
      | case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL11.glColorPointer(size, stride, b)
      | case _ => error("Failed conversion of buffer type: " + pointer.getClass.getName)
      |}""".stripMargin
  }

  val glVertexPointer = new ExplicitMatcher {
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

  val glGenBuffer = new ExplicitMatcher {
    val methodName = "glGenBuffer"
    val args = Nil
    val returnType = classOf[Int]
    val left = List(
      method("android.opengl.GLES11.glGenBuffers(n: Int, buffers: Array[Int], offset: Int): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL15.glGenBuffers(): Int").get
    )
    val androidBody = """val buffers = new Array[Int](1)
      |android.opengl.GLES11.glGenBuffers(1, buffers, 0)
      |buffers(0)""".stripMargin
    val lwjglBody = """org.lwjgl.opengl.GL15.glGenBuffers()"""
  }

  val glGenBuffers = new ExplicitMatcher {
    val methodName = "glGenBuffers"
    val args = List(
      "buffers" -> classOf[java.nio.IntBuffer]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES11.glGenBuffers(n: Int, buffers: java.nio.IntBuffer): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL15.glGenBuffers(buffers: java.nio.IntBuffer): Unit").get
    )
    val androidBody = """android.opengl.GLES11.glGenBuffers(0, buffers)"""
    val lwjglBody = """org.lwjgl.opengl.GL15.glGenBuffers(buffers)"""
  }

  val glGenBuffers2 = new ExplicitMatcher {
    val methodName = "glGenBuffers"
    val args = List(
      "n" -> classOf[Int],
      "buffers" -> classOf[Array[Int]],
      "offset" -> classOf[Int]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES11.glGenBuffers(n: Int, buffers: Array[Int], offset: Int): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL15.glGenBuffers(): Int").get
    )
    val androidBody = """android.opengl.GLES11.glGenBuffers(n, buffers, offset)"""
    val lwjglBody = """if (n > 0) {
      | buffers(offset) = org.lwjgl.opengl.GL15.glGenBuffers()
      | glGenBuffers(n - 1, buffers, offset + 1)
      |}""".stripMargin
  }

  val glGenTexture = new ExplicitMatcher {
    val methodName = "glGenTexture"
    val args = Nil
    val returnType = classOf[Int]
    val left = List(
      method("android.opengl.GLES10.glGenTextures(n: Int, textures: Array[Int], offset: Int): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL11.glGenTextures(): Int").get
    )
    val androidBody = """val buffers = new Array[Int](1)
      |android.opengl.GLES10.glGenTextures(1, buffers, 0)
      |buffers(0)""".stripMargin
    val lwjglBody = """org.lwjgl.opengl.GL11.glGenTextures()"""
  }

  val glGenTextures = new ExplicitMatcher {
    val methodName = "glGenTextures"
    val args = List(
      "textures" -> classOf[IntBuffer]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES10.glGenTextures(n: Int, textures: java.nio.IntBuffer): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL11.glGenTextures(textures: java.nio.IntBuffer): Unit").get
    )
    // TODO: Is "n" count or offset?
    val androidBody = """android.opengl.GLES10.glGenTextures(textures.remaining, textures)"""
    val lwjglBody = """org.lwjgl.opengl.GL11.glGenTextures(textures)"""
  }

  val glDeleteBuffer = new ExplicitMatcher {
    val methodName = "glDeleteBuffer"
    val args = List(
      "id" -> classOf[Int]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES11.glDeleteBuffers(n: Int, buffers: Array[Int], offset: Int): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL15.glDeleteBuffers(buffer: Int): Unit").get
    )
    val androidBody = """android.opengl.GLES11.glDeleteBuffers(1, Array(id), 0)"""
    val lwjglBody = """org.lwjgl.opengl.GL15.glDeleteBuffers(id)"""
  }

  val glDeleteBuffers = new ExplicitMatcher {
    val methodName = "glDeleteBuffers"
    val args = List(
      "n" -> classOf[Int],
      "buffers" -> classOf[Array[Int]],
      "offset" -> classOf[Int]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES11.glDeleteBuffers(n: Int, buffers: Array[Int], offset: Int): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL15.glDeleteBuffers(buffer: Int): Unit").get
    )
    val androidBody = """android.opengl.GLES11.glDeleteBuffers(n, buffers, offset)"""
    val lwjglBody = """for (index <- offset until (offset + n)) {
      | org.lwjgl.opengl.GL15.glDeleteBuffers(buffers(index))
      |}""".stripMargin
  }

  val glBufferData = new ExplicitMatcher {
    val methodName = "glBufferData"
    val args = List(
      "target" -> classOf[Int],
      "size" -> classOf[Int],
      "data" -> classOf[java.nio.Buffer],
      "usage" -> classOf[Int]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES11.glBufferData(target: Int, size: Int, data: java.nio.Buffer, usage: Int): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL15.glBufferData(target: Int, data: java.nio.ByteBuffer, usage: Int): Unit").get,
      method("org.lwjgl.opengl.GL15.glBufferData(target: Int, data: java.nio.ShortBuffer, usage: Int): Unit").get,
      method("org.lwjgl.opengl.GL15.glBufferData(target: Int, data: java.nio.IntBuffer, usage: Int): Unit").get,
      method("org.lwjgl.opengl.GL15.glBufferData(target: Int, data: java.nio.FloatBuffer, usage: Int): Unit").get,
      method("org.lwjgl.opengl.GL15.glBufferData(target: Int, data: java.nio.DoubleBuffer, usage: Int): Unit").get
    )
    val androidBody = """android.opengl.GLES11.glBufferData(target, size, data, usage)"""
    val lwjglBody = """data match {
      | case b: java.nio.ByteBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
      | case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
      | case b: java.nio.IntBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
      | case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
      | case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
      | case _ => error("Failed conversion of buffer type: " + data.getClass.getName)
      |}""".stripMargin
  }

  val glTexImage2D = new ExplicitMatcher {
    val methodName = "glTexImage2D"
    val args = List(
      "target" -> classOf[Int],
      "level" -> classOf[Int],
      "internalFormat" -> classOf[Int],
      "width" -> classOf[Int],
      "height" -> classOf[Int],
      "border" -> classOf[Int],
      "format" -> classOf[Int],
      "type" -> classOf[Int],
      "pixels" -> classOf[java.nio.Buffer]
    )
    val returnType = classOf[Unit]
    val left = List(
      method("android.opengl.GLES10.glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: java.nio.Buffer): Unit").get
    )
    val right = List(
      method("org.lwjgl.opengl.GL11.glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: java.nio.ByteBuffer): Unit").get,
      method("org.lwjgl.opengl.GL11.glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: java.nio.ShortBuffer): Unit").get,
      method("org.lwjgl.opengl.GL11.glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: java.nio.IntBuffer): Unit").get,
      method("org.lwjgl.opengl.GL11.glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: java.nio.FloatBuffer): Unit").get,
      method("org.lwjgl.opengl.GL11.glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: java.nio.DoubleBuffer): Unit").get
    )
    val androidBody = """android.opengl.GLES10.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, pixels)"""
    val lwjglBody = """pixels match {
      | case b: java.nio.ByteBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
      | case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
      | case b: java.nio.IntBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
      | case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
      | case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
      | case _ => error("Failed conversion of buffer type: " + pixels.getClass.getName)
      |}""".stripMargin
  }
}