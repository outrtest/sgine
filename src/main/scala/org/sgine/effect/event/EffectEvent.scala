package org.sgine.effect.event

import org.sgine.effect.Effect

import org.sgine.event.Event

abstract class EffectEvent protected(val effect: Effect, val eventType: Int) extends Event(effect)

class EffectStartEvent(effect: Effect) extends EffectEvent(effect, EffectEvent.Start) {
	def retarget(target: org.sgine.event.Listenable): Event = new EffectStartEvent(target.asInstanceOf[Effect])
}

class EffectPauseEvent(effect: Effect) extends EffectEvent(effect, EffectEvent.Pause) {
	def retarget(target: org.sgine.event.Listenable): Event = new EffectPauseEvent(target.asInstanceOf[Effect])
}

class EffectStopEvent(effect: Effect) extends EffectEvent(effect, EffectEvent.Stop) {
	def retarget(target: org.sgine.event.Listenable): Event = new EffectStopEvent(target.asInstanceOf[Effect])
}

class EffectEndEvent(effect: Effect) extends EffectEvent(effect, EffectEvent.End) {
	def retarget(target: org.sgine.event.Listenable): Event = new EffectEndEvent(target.asInstanceOf[Effect])
}

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