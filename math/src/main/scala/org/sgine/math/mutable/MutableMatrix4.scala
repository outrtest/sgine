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

package org.sgine.math.mutable

import org.sgine.math.{Matrix4 => M4}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class MutableMatrix4 (var m00: Double = 0.0, var m01: Double = 0.0, var m02: Double = 0.0, var m03: Double = 0.0,
               var m10: Double = 0.0, var m11: Double = 0.0, var m12: Double = 0.0, var m13: Double = 0.0,
               var m20: Double = 0.0, var m21: Double = 0.0, var m22: Double = 0.0, var m23: Double = 0.0,
               var m30: Double = 0.0, var m31: Double = 0.0, var m32: Double = 0.0, var m33: Double = 0.0
              ) extends M4 {
  /**
   * Modifies the local copy of this matrix.
   */
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
  ) = {
    this.m00 = m00
    this.m01 = m01
    this.m02 = m02
    this.m03 = m03
    this.m10 = m10
    this.m11 = m11
    this.m12 = m12
    this.m13 = m13
    this.m20 = m20
    this.m21 = m21
    this.m22 = m22
    this.m23 = m23
    this.m30 = m30
    this.m31 = m31
    this.m32 = m32
    this.m33 = m33

    this
  }

  /**
   * Creates a new immutable instance of this Matrix4
   */
  def immutable = new org.sgine.math.immutable.ImmutableMatrix4(m00, m01, m02, m03,
                                                       m10, m11, m12, m13,
                                                       m20, m21, m22, m23,
                                                       m30, m31, m32, m33)

  def mutable = this

  def copy(
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
  ) = new MutableMatrix4(m00, m01, m02, m03,
                         m10, m11, m12, m13,
                         m20, m21, m22, m23,
                         m30, m31, m32, m33)
}

object MutableMatrix4 {
  val Zero = M4.Zero
  val Identity = M4.Identity
}