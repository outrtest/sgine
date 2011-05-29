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

package org.sgine.math.immutable

import org.sgine.math.{Matrix4 => M4}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Matrix4 (val m00: Double = 0.0, val m01: Double = 0.0, val m02: Double = 0.0, val m03: Double = 0.0,
               val m10: Double = 0.0, val m11: Double = 0.0, val m12: Double = 0.0, val m13: Double = 0.0,
               val m20: Double = 0.0, val m21: Double = 0.0, val m22: Double = 0.0, val m23: Double = 0.0,
               val m30: Double = 0.0, val m31: Double = 0.0, val m32: Double = 0.0, val m33: Double = 0.0
              ) extends M4 {
  /**
   * Creates a new Matrix4 with the modified values.
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
  ) = new Matrix4(m00, m01, m02, m03,
                  m10, m11, m12, m13,
                  m10, m21, m22, m23,
                  m30, m31, m32, m33)

  /**
   * Creates a new mutable instance of this Matrix4
   */
  def mutable = new org.sgine.math.mutable.Matrix4(m00, m01, m02, m03,
                                                   m10, m11, m12, m13,
                                                   m20, m21, m22, m23,
                                                   m30, m31, m32, m33)
}

object Matrix4 {
  val Zero = M4.Zero
  val Identity = M4.Identity
}