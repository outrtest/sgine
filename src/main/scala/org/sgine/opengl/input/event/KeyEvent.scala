package org.sgine.opengl.input.event

import org.sgine.event._
import org.sgine.opengl.input._

case class KeyEvent(key: Int, state: Boolean, time: Long, keyChar: Char, controlDown: Boolean, shiftDown: Boolean, metaDown: Boolean, menuDown: Boolean, appsDown: Boolean) extends Event(Keyboard)