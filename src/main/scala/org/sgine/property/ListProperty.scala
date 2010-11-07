package org.sgine.property

import org.sgine.event.EventHandler
import org.sgine.event.Listenable

class ListProperty[T] (value: List[T] = Nil, parent: Listenable = null, name: String = null, dependency: Function0[List[T]] = null, filter: List[T] => List[T] = null, filterType: FilterType = FilterType.Modify, listener: EventHandler = null)(override implicit val manifest: Manifest[List[T]]) extends AdvancedProperty[List[T]](value, parent, name, dependency, filter, filterType, listener) {
	val threadSafe = new AdvancedProperty[Boolean](true, this)
	
	private def add(value: T) = apply(value :: apply())
	
	private def remove(value: T) = apply(apply().filterNot(_ == value))
	
	def +=(value: T) = if (threadSafe()) addSynchronized(value) else add(value)
	
	def -=(value: T) = if (threadSafe()) removeSynchronized(value) else remove(value)
	
	def addSynchronized(value: T) = synchronized {
		add(value)
	}
	
	def removeSynchronized(value: T) = synchronized {
		remove(value)
	}
	
	def size = apply().size
	
	def length = apply().length
}