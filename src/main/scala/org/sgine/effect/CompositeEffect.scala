package org.sgine.effect

import org.sgine.effect.event.EffectEndEvent

import org.sgine.event.EventHandler

// TODO: support Parallel or Sequence
class CompositeEffect(effects: Effect*) extends Effect {
	var index: Int = 0
	private var current: Effect = _
	
	def play() = {
		current = effects(index)
		current.start()
	}
	
	def finished = {
		if ((current != null) && (current.finished)) {
			index += 1
			
			if (index >= effects.length) {
				index = 0
				
				true
			} else {
				current = effects(index)
				current.start()
				
				false
			}
		} else {
			false
		}
	}
}