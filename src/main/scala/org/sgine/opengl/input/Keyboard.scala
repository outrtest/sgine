package org.sgine.opengl.input

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
	
	def update(time: Double) = {
		if (GLKeyboard.isCreated()) {
			try {
				while (GLKeyboard.next()) {
					val state = GLKeyboard.getEventKeyState
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
					val evt = event.KeyEvent(
												GLKeyboard.getEventKey,
												state,
												GLKeyboard.getEventNanoseconds,
												GLKeyboard.getEventCharacter,
												isControlDown,
												isShiftDown,
												isMetaDown,
												isMenuDown,
												isAppsDown
											 )
					Event.enqueue(evt)
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
	
	def isAltDown = isMenuDown
}