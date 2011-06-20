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

package org.sgine.math

import annotation.tailrec
import scala.math._
import java.nio._

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Matrix4 extends Traversable[Double] {
  def m00: Double
  def m01: Double
  def m02: Double
  def m03: Double
  def m10: Double
  def m11: Double
  def m12: Double
  def m13: Double
  def m20: Double
  def m21: Double
  def m22: Double
  def m23: Double
  def m30: Double
  def m31: Double
  def m32: Double
  def m33: Double

  def apply(index: Int) = index match {
    case 0 => m00
    case 1 => m10
    case 2 => m20
    case 3 => m30
    case 4 => m01
    case 5 => m11
    case 6 => m21
    case 7 => m31
    case 8 => m02
    case 9 => m12
    case 10 => m22
    case 11 => m32
    case 12 => m03
    case 13 => m13
    case 14 => m23
    case 15 => m33
    case _ => throw new IndexOutOfBoundsException("Index " + index + " is greater than Matrix4 bounds (15)")
  }

  def apply(
             m00: Double = this.m00,
             m01: Double = this.m01,
             m02: Double = this.m02,
             m03: Double = this.m03,
             m10: Double = this.m10,
             m11: Double = this.m11,
             m12: Double = this.m12,
             m13: Double = this.m13,
             m20: Double = this.m20,
             m21: Double = this.m21,
             m22: Double = this.m22,
             m23: Double = this.m23,
             m30: Double = this.m30,
             m31: Double = this.m31,
             m32: Double = this.m32,
             m33: Double = this.m33
             ): Matrix4

  def apply(m: Matrix4): Matrix4 = apply(
    m.m00, m.m01, m.m02, m.m03,
    m.m10, m.m11, m.m12, m.m13,
    m.m20, m.m21, m.m22, m.m23,
    m.m30, m.m31, m.m32, m.m33
  )

  def identity = apply(Matrix4.Identity)

  /**
   * Creates a new java.nio.DoubleBuffer and sets this Matrix4 to it
   */
  def doubleBuffer = toBuffer(Matrix4.doubleBuffer)

  /**
   * Creates a new java.nio.FloatBuffer and sets this Matrix4 to it
   */
  def floatBuffer = toBuffer(Matrix4.floatBuffer)

  @tailrec
  final def toBuffer(buffer: Buffer, offset: Int = 0, start: Int = 0): Unit = {
    buffer match {
      case b: DoubleBuffer => b.put(offset, apply(start))
      case b: FloatBuffer => b.put(offset, apply(start).toFloat)
      case b: IntBuffer => b.put(offset, apply(start).toInt)
      case _ => sys.error("Unhandled buffer type: " + buffer.getClass.getName)
    }
    if (start >= 15) {
      buffer.position(offset + 1)
      buffer.flip()
    } else {
      toBuffer(buffer, offset + 1, start + 1)
    }
  }

  def translate(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = {
    apply(m00, m01, m02, m03 + x,
      m10, m11, m12, m13 + y,
      m20, m21, m22, m23 + z,
      m30, m31, m32, m33)
  }

  def rotateX(angle: Double) = {
    val sinx = sin(angle)
    val cosx = cos(angle)

    val t10 = cosx * m10 - sinx * m20
    val t20 = sinx * m10 + cosx * m20
    val t11 = cosx * m11 - sinx * m21
    val t21 = sinx * m11 + cosx * m21
    val t12 = cosx * m12 - sinx * m22
    val t22 = sinx * m12 + cosx * m22
    val t13 = cosx * m13 - sinx * m23
    val t23 = sinx * m13 + cosx * m23

    apply(m00, m01, m02, m03,
      t10, t11, t12, t13,
      t20, t21, t22, t23,
      m30, m31, m32, m33)
  }

  def rotateY(angle: Double) = {
    val siny = sin(angle)
    val cosy = cos(angle)

    val t00 = cosy * m00 + siny * m20
    val t20 = cosy * m20 - siny * m00
    val t01 = cosy * m01 + siny * m21
    val t21 = cosy * m21 - siny * m01
    val t02 = cosy * m02 + siny * m22
    val t22 = cosy * m22 - siny * m02
    val t03 = cosy * m03 + siny * m23
    val t23 = cosy * m23 - siny * m03

    apply(t00, t01, t02, t03,
      m10, m11, m12, m13,
      t20, t21, t22, t23,
      m30, m31, m32, m33)
  }

  def rotate(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = {
    val cx = cos(x)
    val sx = sin(x)
    val cy = cos(-y)
    val sy = sin(-y)
    val cz = cos(z)
    val sz = sin(z)

    val cxsy = cx * sy
    val sxsy = sx * sy

    val d00 = cy * cz
    val d01 = -cy * sz
    val d02 = -sy

    val d10 = -sxsy * cz + cx * sz
    val d11 = sxsy * sz + cx * cz
    val d12 = -sx * cy

    val d20 = cxsy * cz + sx * sz
    val d21 = -cxsy * sz + sx * cz
    val d22 = cx * cy

    val t00 = m00 * d00 + m01 * d10 + m02 * d20
    val t01 = m00 * d01 + m01 * d11 + m02 * d21;
    val t02 = m00 * d02 + m01 * d12 + m02 * d22;

    val t10 = m10 * d00 + m11 * d10 + m12 * d20;
    val t11 = m10 * d01 + m11 * d11 + m12 * d21;
    val t12 = m10 * d02 + m11 * d12 + m12 * d22;

    val t20 = m20 * d00 + m21 * d10 + m22 * d20;
    val t21 = m20 * d01 + m21 * d11 + m22 * d21;
    val t22 = m20 * d02 + m21 * d12 + m22 * d22;

    val t30 = m30 * d00 + m31 * d10 + m32 * d20;
    val t31 = m30 * d01 + m31 * d11 + m32 * d21;
    val t32 = m30 * d02 + m31 * d12 + m32 * d22;

    apply(t00, t01, t02, m03,
      t10, t11, t12, m13,
      t20, t21, t22, m23,
      t30, t31, t32, m33)
  }

  def scale(x: Double = 1.0, y: Double = 1.0, z: Double = 1.0) = {
    apply(m00 * x, m01 * x, m02 * x, m03 * x,
      m10 * y, m11 * y, m12 * y, m13 * y,
      m20 * z, m21 * z, m22 * z, m23 * z,
      m30, m31, m32, m33)
  }

  def foreach[U](f: Double => U) = forindexed(0, f)

  @tailrec
  private def forindexed[U](index: Int, f: Double => U): Unit = {
    f(apply(index))
    if (index < 15) forindexed(index + 1, f)
  }

  override def size = 16

  override def toString() = {
    val buffer = new StringBuilder()

    val columnSeparator = "  "
    val rowSeparator = "\n"
    val indent = "         "

    def appendRow(d0: Double, d1: Double, d2: Double, d3: Double) {
      def appendValue(d: Double, s: String) {
        buffer.append(String.format("%5.02f", d.asInstanceOf[java.lang.Double]))
        buffer.append(s)
      }

      buffer.append(indent)
      appendValue(d0, columnSeparator)
      appendValue(d1, columnSeparator)
      appendValue(d2, columnSeparator)
      appendValue(d3, rowSeparator)
    }

    buffer.append("Matrix4(\n")
    appendRow(m00, m01, m02, m03)
    appendRow(m10, m11, m12, m13)
    appendRow(m20, m21, m22, m23)
    appendRow(m30, m31, m32, m33)
    buffer.append(")")

    buffer.toString()
  }
}

object Matrix4 {
  val Zero = new immutable.Matrix4()
  val Identity = new immutable.Matrix4(m00 = 1.0, m11 = 1.0, m22 = 1.0, m33 = 1.0)

  /**
   * Creates a new java.nio.DoubleBuffer capable of storing a Matrix4
   */
  def doubleBuffer = ByteBuffer.allocateDirect(128).order(ByteOrder.nativeOrder).asDoubleBuffer()

  /**
   * Creates a new java.nio.FloatBuffer capable of storing a Matrix4
   */
  def floatBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder).asFloatBuffer()
}