package org.sgine.property.event

import org.sgine.event.Event
import org.sgine.event.Listenable

import org.sgine.property.NumericProperty

import org.sgine.util.Cacheable
import org.sgine.util.ObjectCache

class NumericPropertyChangeEvent protected() extends Event(null) with Cacheable[NumericPropertyChangeEvent] {
	protected var _property: NumericProperty = _
	protected var _oldValue: Double = _
	protected var _newValue: Double = _
	protected var _adjusting: Boolean = _
	
	override def listenable = _property
	def property = _property
	def oldValue = _oldValue
	def newValue = _newValue
	def adjusting = _adjusting
	
	def cache = NumericPropertyChangeEventCache
	
	def retarget(target: Listenable) = NumericPropertyChangeEvent(target.asInstanceOf[NumericProperty], oldValue, newValue, adjusting)
}

object NumericPropertyChangeEvent {
	def apply(property: NumericProperty, oldValue: Double, newValue: Double, adjusting: Boolean) = {
		val pce = NumericPropertyChangeEventCache.request().asInstanceOf[NumericPropertyChangeEvent]
		
		pce._property = property
		pce._oldValue = oldValue
		pce._newValue = newValue
		pce._adjusting = adjusting
		
		pce
	}
}

object NumericPropertyChangeEventCache extends ObjectCache[NumericPropertyChangeEvent](10000) {
	def create() = new NumericPropertyChangeEvent()
}