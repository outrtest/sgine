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

import org.sgine.property.{MutableProperty, PropertyContainer}
import org.sgine.math.mutable.Matrix4
import org.sgine.render.Renderer
import org.sgine.event.{Listenable, ChangeEvent}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 7/13/11
 */
trait TranslationMatrixComponent extends RenderableComponent with MatrixComponent {
  /**
   * Determines whether parent MatrixComponents should be multiplied against this
   * matrix upon change.
   */
  val matrixHierarchy = new MutableProperty[Boolean](true)

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

  // Update the matrix when information changes
  val matrixDirty = dirtyUpdate[ChangeEvent[Double]](scale.x, scale.y, scale.z, position.x, position.y, position.x) {
    matrix(Matrix4.Identity)
    if (matrixHierarchy()) {
      multMatrixHierarchy(parent())
    }
    matrix.scale(scale.x(), scale.y(), scale.z())
    matrix.translate(position.x(), position.y(), position.z())
  }
  
  matrixDirty.invoke()

  private def multMatrixHierarchy(listenable: Listenable): Unit = listenable match {
    case null => // Reached the top
    case tmc: TranslationMatrixComponent if (tmc.matrixHierarchy()) => {
      multMatrixHierarchy(tmc.parent())
      matrix.mult(tmc.matrix)
    }
    case mc: MatrixComponent => {
      println(mc.matrix)
      matrix.mult(mc.matrix)
    }
    case l => {
      multMatrixHierarchy(l.parent())
    }
  }

  override def render() = {
    Renderer().loadMatrix(matrix)
    super.render()
  }
}