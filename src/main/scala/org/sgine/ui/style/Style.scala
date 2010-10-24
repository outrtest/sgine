package org.sgine.ui.style

import org.sgine.core.Color

import org.sgine.event.Listenable

import org.sgine.property.DependentProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.MutableProperty
import org.sgine.property.NamedProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Component

class Style extends PropertyContainer

class StyleProperty[T] private(override protected implicit val manifest: Manifest[T]) extends MutableProperty[T] with DependentProperty[T] with ListenableProperty[T] with NamedProperty[T] {
	protected var _dependency: Function0[T] = _
	def dependency = _dependency
}

object StyleProperty {
	def apply[T](value: T = null, parent: Listenable = null, dependency: Function0[T] = null)(implicit manifest: Manifest[T]) = {
		val p = new StyleProperty[T]()
		p(value)
		p.parent = parent
		p._dependency = dependency
		
		p
	}
}