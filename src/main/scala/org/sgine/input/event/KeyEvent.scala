package org.sgine.input.event

import org.sgine.event.Event
import org.sgine.event.Listenable

import org.sgine.input.Key
import org.sgine.input.Keyboard

abstract class KeyEvent protected(val key: Key,
					val state: KeyState,
					val time: Long,
					val keyChar: Char,
					val controlDown: Boolean,
					val shiftDown: Boolean,
					val metaDown: Boolean,
					val menuDown: Boolean,
					val appsDown: Boolean,
					val capsDown: Boolean,
					val repeat: Boolean,
					listenable: Listenable) extends Event(listenable) {
	override def toString() = key + state.name + ". Modifiers: " + (if (controlDown) "Control " else "") + (if (shiftDown) "Shift " else "") + (if (metaDown) "Meta " else "") + (if (menuDown) "Menu " else "") + (if (appsDown) "Apps " else "") 
}

class KeyPressEvent protected(key: Key,
					time: Long,
					keyChar: Char,
					controlDown: Boolean,
					shiftDown: Boolean,
					metaDown: Boolean,
					menuDown: Boolean,
					appsDown: Boolean,
					capsDown: Boolean,
					repeat: Boolean,
					listenable: Listenable) extends KeyEvent(key, KeyState.Pressed, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, listenable) {
	def retarget(target: org.sgine.event.Listenable): Event = new KeyPressEvent(key, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, target)
}

class KeyReleaseEvent protected(key: Key,
					time: Long,
					keyChar: Char,
					controlDown: Boolean,
					shiftDown: Boolean,
					metaDown: Boolean,
					menuDown: Boolean,
					appsDown: Boolean,
					capsDown: Boolean,
					repeat: Boolean,
					listenable: Listenable) extends KeyEvent(key, KeyState.Released, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, listenable) {
	def retarget(target: org.sgine.event.Listenable): Event = new KeyReleaseEvent(key, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, target)
}

class KeyTypeEvent protected(key: Key,
					time: Long,
					keyChar: Char,
					controlDown: Boolean,
					shiftDown: Boolean,
					metaDown: Boolean,
					menuDown: Boolean,
					appsDown: Boolean,
					capsDown: Boolean,
					repeat: Boolean,
					listenable: Listenable) extends KeyEvent(key, KeyState.Typed, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, listenable) {
	def retarget(target: org.sgine.event.Listenable): Event = new KeyTypeEvent(key, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, target)
}

object KeyEvent {
	def apply(key: Key,
			  state: KeyState,
			  time: Long,
			  keyChar: Char,
			  controlDown: Boolean,
			  shiftDown: Boolean,
			  metaDown: Boolean,
			  menuDown: Boolean,
			  appsDown: Boolean,
			  capsDown: Boolean,
			  repeat: Boolean = false,
			  listenable: Listenable = Keyboard): KeyEvent = {
		state match {
			case KeyState.Pressed => new KeyPressEvent(key, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, listenable)
			case KeyState.Released => new KeyReleaseEvent(key, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, listenable)
			case KeyState.Typed => new KeyTypeEvent(key, time, keyChar, controlDown, shiftDown, metaDown, menuDown, appsDown, capsDown, repeat, listenable)
		}
	}
	
	def apply(target: Listenable, original: KeyEvent): KeyEvent = {
		apply(original.key,
			  original.state,
			  original.time,
			  original.keyChar,
			  original.controlDown,
			  original.shiftDown,
			  original.metaDown,
			  original.menuDown,
			  original.appsDown,
			  original.capsDown,
			  original.repeat,
			  target)
	}
}