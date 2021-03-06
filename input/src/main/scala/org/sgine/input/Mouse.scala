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

package org.sgine.input

import event.{MouseReleaseEvent, MousePressEvent}
import org.powerscala.event.Listenable
import org.powerscala.{Enumerated, EnumEntry}
import org.powerscala.property.{PropertyParent, Property}

/**
 * Mouse represents the singleton of the mouse object.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class Mouse extends EnumEntry[Mouse]()(Mouse)

object Mouse extends Listenable with Enumerated[Mouse] with PropertyParent {
  val parent: PropertyParent = null

  // Update the mouse button state
  listeners.synchronous {
    case evt: MousePressEvent => evt.button._down = true
    case evt: MouseReleaseEvent => evt.button._down = false
  }

  val x = Property[Int]("x", 0)
  val y = Property[Int]("y", 0)

  val Move = new Mouse
  val Press = new Mouse
  val Release = new Mouse
  val Drag = new Mouse
  val Over = new Mouse
  val Out = new Mouse
  val Click = new Mouse
  val Wheel = new Mouse
}