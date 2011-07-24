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
import immutable.ImmutableVector2
import mutable.MutableVector2

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Vector2 extends Traversable[Double] {
  def x: Double
  def y: Double

  def apply(x: Double = this.x, y: Double = this.y): Vector2

  def apply(index: Int) = index match {
    case 0 => x
    case 1 => y
    case _ => throw new IndexOutOfBoundsException("Index " + index + " is greater than Vector2 bounds (1)")
  }

  def apply(v: Vector2): Vector2 = apply(v.x, v.y)

  def foreach[U](f: Double => U) = forIndexed(0, f)

  @tailrec
  private def forIndexed[U](index: Int, f: Double => U): Unit = {
    f(apply(index))
    if (index < 1) forIndexed(index + 1, f)
  }

  override def size = 2

  override def toString() = "Vector2(x = " + x + ", y = " + y + ")"

  def mutable: Vector2

  def immutable: Vector2

  def copy(x: Double = this.x, y: Double = this.y): Vector2

  def isMutable: Boolean

  final def isImmutable = !isMutable
}

object Vector2 {
  def mut(x: Double = 0.0, y: Double = 0.0): Vector2 = new MutableVector2(x, y)

  def immut(x: Double = 0.0, y: Double = 0.0): Vector2 = new ImmutableVector2(x, y)
}