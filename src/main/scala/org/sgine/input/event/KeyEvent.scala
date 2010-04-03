package org.sgine.input.event

import org.sgine.event.Event

import org.sgine.input.Key
import org.sgine.input.Keyboard

// TODO: Update to use focused component instead of Keyboard
case class KeyEvent(key: Key,
					state: Boolean,
					time: Long,
					keyChar: Char,
					controlDown: Boolean,
					shiftDown: Boolean,
					metaDown: Boolean,
					menuDown: Boolean,
					appsDown: Boolean,
					capsDown: Boolean) extends Event(Keyboard) {
	override def toString() = key + " (" + keyChar + ") " + (if (state) "Pressed" else "Released") + " Modifiers: " + (if (controlDown) "Control " else "") + (if (shiftDown) "Shift " else "") + (if (metaDown) "Meta " else "") + (if (menuDown) "Menu " else "") + (if (appsDown) "Apps " else "") 
}