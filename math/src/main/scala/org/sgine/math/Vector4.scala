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
import immutable.ImmutableVector4
import mutable.MutableVector4

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Vector4 extends Traversable[Double] {
  def x: Double
  def y: Double
  def z: Double
  def w: Double

  def apply(x: Double = this.x, y: Double = this.y, z: Double = this.z, w: Double = this.w): Vector4

  def apply(index: Int) = index match {
    case 0 => x
    case 1 => y
    case 2 => z
    case 3 => w
    case _ => throw new IndexOutOfBoundsException("Index " + index + " is greater than Vector4 bounds (3)")
  }

  def apply(v: Vector4): Vector4 = apply(v.x, v.y, v.z, v.w)

  def foreach[U](f: Double => U) = forIndexed(0, f)

  @tailrec
  private def forIndexed[U](index: Int, f: Double => U): Unit = {
    f(apply(index))
    if (index < 3) forIndexed(index + 1, f)
  }

  override def size = 4

  override def toString() = "Vector4(x = " + x + ", y = " + y + ", z = " + z + ", w = " + w + ")"

  def mutable: Vector4

  def immutable: Vector4

  def copy(x: Double = this.x, y: Double = this.y, z: Double = this.z, w: Double = this.w): Vector4

  def isMutable: Boolean

  final def isImmutable = !isMutable
}

object Vector4 {
  def mut(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, w: Double = 0.0): Vector4 = new MutableVector4(x, y, z, w)

  def immut(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, w: Double = 0.0): Vector4 = new ImmutableVector4(x, y, z, w)
}