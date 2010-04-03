package org.sgine.input

import org.sgine._
import org.sgine.event._
import org.sgine.work.unit._

import org.lwjgl.input.{Keyboard => GLKeyboard}
import org.lwjgl.input.Keyboard._

object Keyboard extends Listenable with Updatable {
	val parent = null
	val updater = Updater(this)
	Event.workManager += RepeatingUnit(updater)
	
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
	
	def update(time: Double) = {
		if (GLKeyboard.isCreated()) {
			try {
				while (GLKeyboard.next()) {
					val state = GLKeyboard.getEventKeyState
					
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
					if ((!caps) && (!isShiftDown) && (Character.isUpperCase(GLKeyboard.getEventCharacter))) {
						println("Caps on!")
						caps = true
					} else if ((caps) && (!isShiftDown) && (Character.isLowerCase(GLKeyboard.getEventCharacter))) {
						println("Caps off!")
						caps = false
					}
					val key = Key(GLKeyboard.getEventKey, isShiftDown, isCapsDown)
					if (key == null) {
						println("org.sgine.input.Keyboard.update: Unable to find key: " + GLKeyboard.getEventKey + " - " + isShiftDown + " - " + isCapsDown + " - state: " + state + ", " + GLKeyboard.getKeyName(GLKeyboard.getEventKey))
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
				case e: IllegalStateException => // Ignore - timing issue
			}
		}
	}
	
	def validate() = {
		if (!GLKeyboard.isCreated()) {
			GLKeyboard.create()
		}
	}
	
	def isControlDown = lControl || rControl
	def isShiftDown = lShift || rShift
	def isMetaDown = lMeta || rMeta
	def isMenuDown = lMenu || rMenu
	def isAppsDown = apps
	def isCapsDown = caps
	
	def isAltDown = isMenuDown
}