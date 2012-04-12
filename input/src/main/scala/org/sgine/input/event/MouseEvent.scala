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

package org.sgine.input.event

import org.sgine.input.{MouseButton, Mouse}
import org.sgine.event.{Listenable, Event}

/**
 * MouseEvents
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait MouseEvent extends Event {
  def state: Mouse

  def x: Double

  def y: Double

  def z: Double

  def deltaX: Double

  def deltaY: Double

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z): MouseEvent
}

trait MouseButtonEvent extends MouseEvent {
  def button: MouseButton
}

case class MousePressEvent(button: MouseButton, x: Double, y: Double, z: Double, deltaX: Double, deltaY: Double, target: Listenable = Mouse) extends MouseButtonEvent {
  val state = Mouse.Press

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z) = copy(target = newTarget, x = x, y = y, z = z)
}

case class MouseReleaseEvent(button: MouseButton, x: Double, y: Double, z: Double, deltaX: Double, deltaY: Double, target: Listenable = Mouse) extends MouseButtonEvent {
  val state = Mouse.Release

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z) = copy(target = newTarget, x = x, y = y, z = z)
}

case class MouseClickEvent(button: MouseButton, x: Double, y: Double, z: Double, deltaX: Double, deltaY: Double, target: Listenable = Mouse) extends MouseButtonEvent {
  val state = Mouse.Click

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z) = copy(target = newTarget, x = x, y = y, z = z)
}

case class MouseMoveEvent(x: Double, y: Double, z: Double, deltaX: Double, deltaY: Double, target: Listenable = Mouse) extends MouseEvent {
  val state = Mouse.Move

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z) = copy(target = newTarget, x = x, y = y, z = z)
}

case class MouseDragEvent(x: Double, y: Double, z: Double, deltaX: Double, deltaY: Double, target: Listenable = Mouse) extends MouseEvent {
  val state = Mouse.Drag

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z) = copy(target = newTarget, x = x, y = y, z = z)
}

case class MouseOverEvent(x: Double, y: Double, z: Double, deltaX: Double, deltaY: Double, target: Listenable = Mouse) extends MouseEvent {
  val state = Mouse.Over

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z) = copy(target = newTarget, x = x, y = y, z = z)
}

case class MouseOutEvent(x: Double, y: Double, z: Double, deltaX: Double, deltaY: Double, target: Listenable = Mouse) extends MouseEvent {
  val state = Mouse.Out

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z) = copy(target = newTarget, x = x, y = y, z = z)
}

case class MouseWheelEvent(wheel: Int, x: Double, y: Double, z: Double, deltaX: Double, deltaY: Double, target: Listenable = Mouse)
  extends MouseEvent {
  val state = Mouse.Wheel

  def duplicate(newTarget: Listenable, x: Double = x, y: Double = y, z: Double = z) = copy(target = newTarget, x = x, y = y, z = z)
}