package org.sgine.effect

import org.sgine.effect.event.EffectEvent

import org.sgine.event.Event
import org.sgine.event.Listenable

import org.sgine.work.Updatable

trait Effect extends Listenable with Updatable {
	var repeat: Int = 0
	
	private var _playing: Boolean = false
	
	def playing = _playing
	
	private var repeated: Int = 0
	
	def start() = {
		initUpdatable()
		Event.enqueue(EffectEvent(EffectEvent.Start, this))			// Throw start event
		
		play()
		_playing = true
		Effect.started(this)
	}
	
	protected def play(): Unit
	
	def finished: Boolean
	
	abstract override def update(time: Double) = {
		super.update(time)
		
		if ((_playing) && (finished)) {
			_playing = false
			
			Event.enqueue(EffectEvent(EffectEvent.End, this))			// Throw end event
		
			if ((repeat == -1) || (repeated < repeat)) {
				repeated += 1
				
				start()
			} else {
				Effect.finished(this)
			}
		}
	}
	
	def resume(): Boolean = false
	
	def pause(): Boolean = false
	
	def stop(): Boolean = false
}

object Effect {
	// Keep a list of all active effects to keep them from being gc'd
	private var active: List[Effect] = Nil
	
	protected def started(effect: Effect) = {
		synchronized {
			active = effect :: active
		}
	}
	
	protected def finished(effect: Effect) = {
		synchronized {
			active = active.filterNot(_ == effect)
		}
	}
}