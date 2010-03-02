package org.sgine.property

import org.sgine.event._

import org.sgine.property.container._

class AdvancedProperty[T] extends MutableProperty[T] with ListenableProperty[T] with NamedProperty with BindingProperty[T] with AdjustableProperty[T] with EventDelegationProperty[T] {
	private var _name: String = _
	
	override lazy val name = determineName
	
	def this(value: T) = {
		this()
		
		apply(value)
	}
	
	def this(value: T, parent: Listenable) = {
		this(value)
		
		this.parent = parent
	}
	
	def this(value: T, parent: Listenable, name: String) = {
		this(value, parent)
		
		_name = name
	}
	
	private def determineName: String = {
		if (_name != null) {
			_name
		} else if (parent != null) {
			parent match {
				case pc: PropertyContainer => pc.name(this)
				case _ => null
			}
		} else {
			null
		}
	}
}