package org.sgine.opengl.input.event

import org.sgine.event._
import org.sgine.opengl.input._

case class KeyEvent(key: Int, state: Boolean, time: Long, keyChar: Char) extends Event(Keyboard)