package org.sgine.property.event

import java.util.concurrent.ArrayBlockingQueue

import org.sgine.event._
import org.sgine.property._

import org.sgine.util.Cacheable
import org.sgine.util.ObjectCache

class PropertyChangeEvent[T] protected() extends Event(null) with Cacheable[PropertyChangeEvent[_]] {
	protected var _property: ListenableProperty[T] = _
	protected var _oldValue: T = _
	protected var _newValue: T = _
	protected var _adjusting: Boolean = _
	
	override def listenable = _property
	def property = _property
	def oldValue = _oldValue
	def newValue = _newValue
	def adjusting = _adjusting
	
	def cache = PropertyChangeEventCache
}

object PropertyChangeEvent {
	def apply[T](property: ListenableProperty[T], oldValue: T, newValue: T, adjusting: Boolean) = {
		val pce = PropertyChangeEventCache.request().asInstanceOf[PropertyChangeEvent[T]]
		
		pce._property = property
		pce._oldValue = oldValue
		pce._newValue = newValue
		pce._adjusting = adjusting
		
		pce
	}
}

object PropertyChangeEventCache extends ObjectCache[PropertyChangeEvent[_]] {
	def create() = new PropertyChangeEvent[Any]()
}