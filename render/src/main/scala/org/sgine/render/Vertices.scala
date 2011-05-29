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

package org.sgine.render

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Vertices {
  def triangle(x: Float = 0.0f, y: Float = 0.0f, z: Float = 0.0f, width: Float = 2.0f, height: Float = 2.0f) = {
    val test = y + (height / 2.0f)
    List(
      x, (y + (height / 2.0f)).toFloat, z,                      // Top point
      (x - (width / 2.0f)).toFloat, (y - (height / 2.0f)).toFloat, z,     // Bottom left point
      (x + (width / 2.0f)).toFloat, (y - (height / 2.0f)).toFloat, z      // Bottom right point
    )
  }

  def quad(x1: Float = -1.0f, y1: Float = 1.0f, x2: Float = 1.0f, y2: Float = -1.0f, z: Float = 0.0f) = {
    List(
      x1, y1, 0.0f,       // Top left
      x2, y1, 0.0f,       // Top right
      x1, y2, 0.0f,       // Bottom left
      x2, y2, 0.0f,       // Bottom right
      x1, y2, 0.0f,       // Bottom left
      x2, y1, 0.0f       // Top right
    )
  }
}