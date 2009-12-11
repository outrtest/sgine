package org.sgine.opengl.input

import org.sgine._
import org.sgine.event._
import org.sgine.work.unit._

import org.lwjgl.input.{Keyboard => GLKeyboard}

object Keyboard extends Listenable with Updatable {
	val parent = null
	val updater = Updater(this)
	Event.workManager += RepeatingUnit(updater)
	
	listeners += EventHandler(test)
	
	def update(time: Double) = {
		if (GLKeyboard.isCreated()) {
			while (GLKeyboard.next()) {
				val evt = event.KeyEvent(GLKeyboard.getEventKey, GLKeyboard.getEventKeyState, GLKeyboard.getEventNanoseconds, GLKeyboard.getEventCharacter)
				Event.enqueue(evt)
			}
		}
	}
	
	def validate() = {
		if (!GLKeyboard.isCreated()) {
			GLKeyboard.create()
		}
	}
	
	def test(evt: event.KeyEvent) = println(evt)
}