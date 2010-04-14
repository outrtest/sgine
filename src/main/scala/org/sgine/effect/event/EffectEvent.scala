package org.sgine.effect.event

import org.sgine.effect.Effect

import org.sgine.event.Event

class EffectEvent protected(val effect: Effect, val eventType: Int) extends Event(effect)

class EffectStartEvent(effect: Effect) extends EffectEvent(effect, EffectEvent.Start)

class EffectPauseEvent(effect: Effect) extends EffectEvent(effect, EffectEvent.Pause)

class EffectStopEvent(effect: Effect) extends EffectEvent(effect, EffectEvent.Stop)

class EffectEndEvent(effect: Effect) extends EffectEvent(effect, EffectEvent.End)

object EffectEvent {
	val Start = 1
	val Pause = 2
	val Stop = 3
	val End = 4
	
	def apply(eventType: Int, effect: Effect): EffectEvent = eventType match {
		case Start => new EffectStartEvent(effect)
		case Pause => new EffectPauseEvent(effect)
		case Stop => new EffectStopEvent(effect)
		case End => new EffectEndEvent(effect)
	}
}