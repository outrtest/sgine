package org.sgine.input

import org.sgine.event.Event
import org.sgine.event.Listenable

import org.sgine.input.event.KeyState

import org.sgine.log._

import org.sgine.work.Updatable

import org.lwjgl.input.{Keyboard => GLKeyboard}
import org.lwjgl.input.Keyboard._

object Keyboard extends Listenable with Updatable {
	private var lControl = false
	private var rControl = false
	private var lShift = false
	private var rShift = false
	private var lMeta = false
	private var rMeta = false
	private var lMenu = false
	private var rMenu = false
	private var apps = false
	private var caps = false
	
	def isControlDown = lControl || rControl
	def isShiftDown = lShift || rShift
	def isMetaDown = lMeta || rMeta
	def isMenuDown = lMenu || rMenu
	def isAppsDown = apps
	def isCapsDown = caps
	
	def isAltDown = isMenuDown
	
	def validate() = {
		if (!GLKeyboard.isCreated) {
			GLKeyboard.create()
		}
		GLKeyboard.enableRepeatEvents(true)
		
		initUpdatable()
	}
	
	override def update(time: Double) = {
		super.update(time)
		
		if (GLKeyboard.isCreated) {
			try {
				while (GLKeyboard.next()) {
					val keyState = GLKeyboard.getEventKeyState
					val state = keyState match {
						case true => KeyState.Pressed
						case false => KeyState.Released
					}
					
					// Toggle caps when caps is pressed
					if ((GLKeyboard.getEventKey == GLKeyboard.KEY_CAPITAL) && (keyState)) {
						caps = !caps
					}
					
					GLKeyboard.getEventKey match {
						case KEY_LCONTROL => lControl = keyState
						case KEY_RCONTROL => rControl = keyState
						case KEY_LSHIFT => lShift = keyState
						case KEY_RSHIFT => rShift = keyState
						case KEY_LMETA => lMeta = keyState
						case KEY_RMETA => rMeta = keyState
						case KEY_LMENU => lMenu = keyState
						case KEY_RMENU => rMenu = keyState
						case KEY_APPS => apps = keyState
						case _ =>
					}
					// Work-around for inability to determine caps-lock state
					if ((!caps) && (!isShiftDown) && (Character.isUpperCase(GLKeyboard.getEventCharacter))) {
						caps = true
					} else if ((caps) && (!isShiftDown) && (Character.isLowerCase(GLKeyboard.getEventCharacter))) {
						caps = false
					}
					val key = Key(GLKeyboard.getEventKey, isShiftDown, isCapsDown)
					if (key == null) {
						warn("org.sgine.input.Keyboard.update: Unable to find key: " + GLKeyboard.getEventKey + " - " + isShiftDown + " - " + isCapsDown + " - state: " + state + ", " + GLKeyboard.getKeyName(GLKeyboard.getEventKey))
					} else {
						val evt = event.KeyEvent(
													key,
													state,
													GLKeyboard.getEventNanoseconds,
													key.char,
													isControlDown,
													isShiftDown,
													isMetaDown,
													isMenuDown,
													isAppsDown,
													isCapsDown,
													GLKeyboard.isRepeatEvent
												 )
						Event.enqueue(evt)
					}
					if (state == KeyState.Released) {
						// Throw typed event
						val evt = event.KeyEvent(
													key,
													KeyState.Typed,
													GLKeyboard.getEventNanoseconds,
													key.char,
													isControlDown,
													isShiftDown,
													isMetaDown,
													isMenuDown,
													isAppsDown,
													isCapsDown
												 )
						Event.enqueue(evt)
					}
				}
			} catch {
				case exc: IllegalStateException => trace("Keyboard state error", exc)
				case exc => throw exc
			}
		}
	}
}