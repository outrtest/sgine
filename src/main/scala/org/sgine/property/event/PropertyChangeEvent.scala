package org.sgine.property.event

import org.sgine.event._

import org.sgine.path.PathElementChangeEvent

import org.sgine.property._

import org.sgine.util.Cacheable
import org.sgine.util.ObjectCache

class PropertyChangeEvent[T] protected() extends Event(null) with PathElementChangeEvent { //with Cacheable[PropertyChangeEvent[_]] {
	protected var _property: Property[T] with Listenable = _
	protected var _oldValue: T = _
	protected var _newValue: T = _
	protected var _adjusting: Boolean = _
	
	override def listenable = _property
	def property = _property
	def oldValue = _oldValue
	def newValue = _newValue
	def adjusting = _adjusting
	
//	def cache = PropertyChangeEventCache
	
	def retarget(target: org.sgine.event.Listenable): Event = PropertyChangeEvent(target.asInstanceOf[ListenableProperty[T]], oldValue, newValue, adjusting)
}

object PropertyChangeEvent {
	def apply[T](property: Property[T] with Listenable, oldValue: T, newValue: T, adjusting: Boolean) = {
//		val pce = PropertyChangeEventCache.request().asInstanceOf[PropertyChangeEvent[T]]
		val pce = new PropertyChangeEvent[T]()
		pce._property = property
		pce._oldValue = oldValue
		pce._newValue = newValue
		pce._adjusting = adjusting
		
		pce
	}
}

object PropertyChangeEventCache extends ObjectCache[PropertyChangeEvent[_]](10000) {
	def create() = new PropertyChangeEvent[Any]()
}