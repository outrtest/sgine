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

import event.{ColorChangeEvent, ColorChangeEventSupport}
import org.sgine.property._
import org.sgine.Color
import org.sgine.event.{Recursion, EventHandler, Listenable, ChangeEvent}

/**
 * ColorComponent introduces the ability to configure a color for this Component that will be applied to all children if
 * this is a Container.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait ColorComponent extends Component with DirtyUpdatable with ColorChangeEventSupport {
  protected[ui] val backingColor = Color.White.toMutable

  object color extends PropertyContainer {
    val red = new MutableProperty[Double](1.0)
    val green = new MutableProperty[Double](1.0)
    val blue = new MutableProperty[Double](1.0)
    val alpha = new MutableProperty[Double](1.0)
  }

  private val colorDirty = dirtyUpdate[ChangeEvent[Double]]("colorDirty", color.red, color.green, color.blue, color.alpha) {
    backingColor(color.red, color.green, color.blue, color.alpha)
    multColorHierarchy(parent())
    colorChange.fire(new ColorChangeEvent)
  }
  private val parentColorHandler = EventHandler[ColorChangeEvent]() {
    case evt => colorDirty.flag.set(true)
  }
  private var parentColorComponent: ColorComponent = null
  private def updateParentColorHandler() = {
    val newParentColorComponent = ancestorByType[ColorComponent].getOrElse(null)
    if (newParentColorComponent != parentColorComponent) {
      if (parentColorComponent != null) {
        parentColorComponent.colorChange -= parentColorHandler
      }
      if (newParentColorComponent != null) {
        newParentColorComponent.colorChange += parentColorHandler
      }
      parentColorComponent = newParentColorComponent
      colorDirty.flag.set(true)
    }
  }
  parent.change.listen(recursion = Recursion.Parents) {
    case evt if (evt.name == "parent") => updateParentColorHandler()
    case _ =>
  }
  updateParentColorHandler()
  colorDirty.flag.set(true)

  private def multColorHierarchy(listenable: Listenable): Unit = listenable match {
    case null => // Reached the top
    case cc: ColorComponent => backingColor.multiply(cc.color.red, cc.color.green, cc.color.blue, cc.color.alpha)
    case l => multColorHierarchy(l.parent())
  }
}