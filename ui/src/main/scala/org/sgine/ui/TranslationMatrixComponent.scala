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

import event.MatrixChangeEvent
import org.sgine.property.{MutableProperty, PropertyContainer}
import org.sgine.math.mutable.Matrix4
import org.sgine.event.{Recursion, EventHandler, Listenable, ChangeEvent}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 7/13/11
 */
trait TranslationMatrixComponent extends Component with DirtyUpdatable with MatrixComponent {
  /**
   * Determines whether parent MatrixComponents should be multiplied against this
   * matrix upon change.
   */
  val matrixHierarchy = new MutableProperty[Boolean](true)

  object scale extends PropertyContainer {
    val x = new MutableProperty[Double](1.0)
    val y = new MutableProperty[Double](1.0)
    val z = new MutableProperty[Double](1.0)

    def apply(value: Double = 1.0) = {
      x := value
      y := value
      z := value
    }
  }

  object position extends PropertyContainer {
    val x = new MutableProperty[Double](0.0)
    val y = new MutableProperty[Double](0.0)
    val z = new MutableProperty[Double](0.0)
  }

  def resolution(width: Double, height: Double) = {
    val scale = 165.5 / height
    this.scale(scale)
    position.z := -200.0
    matrixHierarchy := false
  }

  // Update the matrix when information changes
  private val matrixDirty = dirtyUpdate[ChangeEvent[Double]]("translationMatrixDirty", scale.x, scale.y, scale.z, position.x, position.y, position.z) {
    matrix(Matrix4.Identity)
    if (matrixHierarchy()) {
      multMatrixHierarchy(parent())
    }
    matrix.scale(scale.x(), scale.y(), scale.z())
    matrix.translate(position.x(), position.y(), position.z())
    matrixChange.fire(new MatrixChangeEvent)
  }
  private val parentMatrixHandler = EventHandler[MatrixChangeEvent]() {
    case evt => matrixDirty.flag.set(true)
  }
  private var parentMatrixComponent: MatrixComponent = null
  private def updateParentMatrixHandler() = {
    val newParentMatrixComponent = ancestorByType[MatrixComponent].getOrElse(null)
    if (newParentMatrixComponent != parentMatrixComponent) {
      if (parentMatrixComponent != null) {
        parentMatrixComponent.matrixChange -= parentMatrixHandler
      }
      if (newParentMatrixComponent != null) {
        newParentMatrixComponent.matrixChange += parentMatrixHandler
      }
      parentMatrixComponent = newParentMatrixComponent
      matrixDirty.flag.set(true)
    }
  }
  parent.change.listen(recursion = Recursion.Parents) {
    case evt if (evt.name == "parent") => updateParentMatrixHandler()
    case _ =>
  }
  updateParentMatrixHandler()

  private def multMatrixHierarchy(listenable: Listenable): Unit = listenable match {
    case null => // Reached the top
    case mc: MatrixComponent => matrix.mult(mc.matrix)
    case l => multMatrixHierarchy(l.parent())
  }
}