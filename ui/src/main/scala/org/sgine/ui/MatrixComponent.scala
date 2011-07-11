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

package org.sgine.ui

import org.sgine.event.ChangeEvent
import org.sgine.math.mutable.Matrix4
import org.sgine.property.{PropertyContainer, MutableProperty}
import org.sgine.render.Renderer

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait MatrixComponent extends RenderableComponent {

  object scale extends PropertyContainer {
    val x = new MutableProperty[Double](1.0)
    val y = new MutableProperty[Double](1.0)
    val z = new MutableProperty[Double](1.0)
  }

  object position extends PropertyContainer {
    val x = new MutableProperty[Double](0.0)
    val y = new MutableProperty[Double](0.0)
    val z = new MutableProperty[Double](0.0)
  }

  protected val matrix = Matrix4.Identity.mutable

  // Update the matrix when information changes
  dirtyUpdate[ChangeEvent[Double]](scale.x, scale.y, scale.z, position.x, position.y, position.x) {
    matrix.scale(scale.x(), scale.y(), scale.z())
    matrix.translate(position.x(), position.y(), position.z())
  }

  override def render() = {
    Renderer().loadMatrix(matrix)
    super.render()
  }
}