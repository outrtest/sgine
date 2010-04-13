package org.sgine.input.event

import org.sgine.event.Event

import org.sgine.input.Key
import org.sgine.input.Keyboard

class KeyEvent protected(val key: Key,
					val state: Boolean,
					val time: Long,
					val keyChar: Char,
					val controlDown: Boolean,
					val shiftDown: Boolean,
					val metaDown: Boolean,
					val menuDown: Boolean,
					val appsDown: Boolean,
					val capsDown: Boolean) extends Event(Keyboard) {
	override def toString() = key + (if (state) " Pressed" else " Released") + ". Modifiers: " + (if (controlDown) "Control " else "") + (if (shiftDown) "Shift " else "") + (if (metaDown) "Meta " else "") + (if (menuDown) "Menu " else "") + (if (appsDown) "Apps " else "") 
}

class KeyPressEvent protected(key: Key,
					time: Long,
					keyChar: Char,
					controlDown: Boolean,
					shiftDown: Boolean,
					metaDown: Boolean,
					menuDown: Boolean,
					appsDown: Boolean,
					capsDown: Boolean) extends KeyEvent(key, true, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown)

class KeyReleaseEvent protected(key: Key,
					time: Long,
					keyChar: Char,
					controlDown: Boolean,
					shiftDown: Boolean,
					metaDown: Boolean,
					menuDown: Boolean,
					appsDown: Boolean,
					capsDown: Boolean) extends KeyEvent(key, false, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown)

object KeyEvent {
	def apply(key: Key,
			  state: Boolean,
			  time: Long,
			  keyChar: Char,
			  controlDown: Boolean,
			  shiftDown: Boolean,
			  metaDown: Boolean,
			  menuDown: Boolean,
			  appsDown: Boolean,
			  capsDown: Boolean) = {
		if (state) {
			new KeyPressEvent(key, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown)
		} else {
			new KeyReleaseEvent(key, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown)
		}
	}
}