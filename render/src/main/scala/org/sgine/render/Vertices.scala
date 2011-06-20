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
  def triangle(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, width: Double = 2.0, height: Double = 2.0) = {
    val test = y + (height / 2.0)
    List(
      x, (y + (height / 2.0)), z,                      // Top point
      (x - (width / 2.0)), (y - (height / 2.0)), z,     // Bottom left point
      (x + (width / 2.0)), (y - (height / 2.0)), z      // Bottom right point
    )
  }

  def quad(x1: Double = -1.0, y1: Double = 1.0, x2: Double = 1.0, y2: Double = -1.0, z: Double = 0.0) = {
    List(
      x1, y1, 0.0,       // Top left
      x2, y1, 0.0,       // Top right
      x1, y2, 0.0,       // Bottom left
      x2, y2, 0.0,       // Bottom right
      x1, y2, 0.0,       // Bottom left
      x2, y1, 0.0       // Top right
    )
  }

  def rect(width: Double, height: Double) = quad((-width / 2.0), (height / 2.0), (width / 2.0), (-height / 2.0))

  def rectLeft(width: Double, height: Double) = quad(0.0, height, width, 0.0)

  def box(width: Double, height: Double, depth: Double) = {
    val w = width / 2.0
    val h = height / 2.0
    val d = depth / 2.0
    List(
      // Front Face
      -w, h, d,
      w, h, d,
      -w, -h, d,
      w, -h, d,
      -w, -h, d,
      w, h, d,
      // Back Face
      -w, h, -d,
      w, h, -d,
      -w, -h, -d,
      w, -h, -d,
      -w, -h, -d,
      w, h, -d,
      // Left Face
      -w, h, -d,
      -w, h, d,
      -w, -h, -d,
      -w, -h, d,
      -w, -h, -d,
      -w, h, d,
      // Right Face
      w, h, -d,
      w, h, d,
      w, -h, -d,
      w, -h, d,
      w, -h, -d,
      w, h, d,
      // Top Face
      -w, h, -d,
      w, h, -d,
      -w, h, d,
      w, h, d,
      -w, h, d,
      w, h, -d,
      // Bottom Face
      -w, -h, -d,
      w, -h, -d,
      -w, -h, d,
      w, -h, d,
      -w, -h, d,
      w, -h, -d
    )
  }

  def rectCoords(x: Double, y: Double, width: Double, height: Double, texture: Texture) = {
    val left = x / texture.width
    val right = (x + width) / texture.width
    val top = y / texture.height
    val bottom = (y + height) / texture.height
    List(
      left, top,
      right, top,
      left, bottom,
      right, bottom,
      left, bottom,
      right, top
    )
  }
}