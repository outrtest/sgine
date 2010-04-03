package org.sgine.input.event

import org.sgine.event._
import org.sgine.input._

case class KeyEvent(key: Key, state: Boolean, time: Long, keyChar: Char, controlDown: Boolean, shiftDown: Boolean, metaDown: Boolean, menuDown: Boolean, appsDown: Boolean, capsDown: Boolean) extends Event(Keyboard) {
	override def toString() = key + " (" + keyChar + ") " + (if (state) "Pressed" else "Released") + " Modifiers: " + (if (controlDown) "Control " else "") + (if (shiftDown) "Shift " else "") + (if (metaDown) "Meta " else "") + (if (menuDown) "Menu " else "") + (if (appsDown) "Apps " else "") 
}