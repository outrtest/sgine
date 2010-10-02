package org.sgine.input

import org.sgine.event.Event
import org.sgine.event.Listenable

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
		
		initUpdatable()
	}
	
	override def update(time: Double) = {
		super.update(time)
		
		if (GLKeyboard.isCreated) {
			try {
				while (GLKeyboard.next()) {
					val state = GLKeyboard.getEventKeyState
					
					// Toggle caps when caps is pressed
					if ((GLKeyboard.getEventKey == GLKeyboard.KEY_CAPITAL) && (state)) {
						caps = !caps
					}
					
					GLKeyboard.getEventKey match {
						case KEY_LCONTROL => lControl = state
						case KEY_RCONTROL => rControl = state
						case KEY_LSHIFT => lShift = state
						case KEY_RSHIFT => rShift = state
						case KEY_LMETA => lMeta = state
						case KEY_RMETA => rMeta = state
						case KEY_LMENU => lMenu = state
						case KEY_RMENU => rMenu = state
						case KEY_APPS => apps = state
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