package org.sgine.property

import org.sgine.event._

import org.sgine.property.container._

import scala.reflect.Manifest

class AdvancedProperty[T] private(override protected implicit val manifest: Manifest[T]) extends MutableProperty[T] with DependentProperty[T] with ListenableProperty[T] with NamedProperty[T] with BindingProperty[T] with AnimatingProperty[T] with EventDelegationProperty[T] with FilteredProperty[T] {
	protected var _name: String = _
	protected var _filter: T => T = _
	protected var _filterType: FilterType = _
	
	var dependency: Function0[T] = _
	def filter = _filter
	def filterType = _filterType
	
	override def name = _name match {
		case null => super.name
		case n => n
	}
	
	def this(value: T = null, parent: Listenable = null, name: String = null, dependency: Function0[T] = null, filter: T => T = null, filterType: FilterType = FilterType.Modify, listener: EventHandler = null)(implicit manifest: Manifest[T]) = {
		this()

		apply(value)
		this.parent = parent
		_name = name
		this.dependency = dependency
		_filter = filter
		_filterType = filterType
		if (listener != null) {
			listeners += listener
		}
	}
}