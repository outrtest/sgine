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

import org.sgine.input.Mouse
import org.sgine.event.Event

/**
 * MouseEvents
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait MouseEvent extends Event {
  def state: Mouse
  def x: Double
  def y: Double
  def deltaX: Double
  def deltaY: Double
}

object MouseEvent {
  def apply(button: Int, state: Boolean, wheel: Int, x: Double, y: Double, dx: Double, dy: Double): MouseEvent = {
    if (button == -1) {
      // Not a button event
      if (wheel != 0) {
        // Wheel
        MouseWheelEvent(wheel, x, y, dx, dy)
      } else {
        // Move
        MouseMoveEvent(x, y, dx, dy)
      }
    } else if (button == -2) {
      // MouseOver
      MouseOverEvent(x, y, dx, dy)
    } else if (button == -3) {
      // MouseOut
      MouseOutEvent(x, y, dx, dy)
    } else {
      // Button event
      if (state) {
        // Pressed
        MousePressEvent(button, x, y, dx, dy)
      } else {
        // Released
        MouseReleaseEvent(button, x, y, dx, dy)
      }
    }
  }
}

trait MouseButtonEvent extends MouseEvent {
  def button: Int
}

case class MousePressEvent(button: Int, x: Double, y: Double, deltaX: Double, deltaY: Double) extends MouseButtonEvent {
  val state = Mouse.Press
}

case class MouseReleaseEvent(button: Int, x: Double, y: Double, deltaX: Double, deltaY: Double) extends MouseButtonEvent {
  val state = Mouse.Release
}

case class MouseClickEvent(button: Int, x: Double, y: Double, deltaX: Double, deltaY: Double) extends MouseButtonEvent {
  val state = Mouse.Click
}

case class MouseMoveEvent(x: Double, y: Double, deltaX: Double, deltaY: Double) extends MouseEvent {
  val state = Mouse.Move
}

case class MouseOverEvent(x: Double, y: Double, deltaX: Double, deltaY: Double) extends MouseEvent {
  val state = Mouse.Over
}

case class MouseOutEvent(x: Double, y: Double, deltaX: Double, deltaY: Double) extends MouseEvent {
  val state = Mouse.Out
}

case class MouseWheelEvent(wheel: Int, x: Double, y: Double, deltaX: Double, deltaY: Double) extends MouseEvent {
  val state = Mouse.Wheel
}