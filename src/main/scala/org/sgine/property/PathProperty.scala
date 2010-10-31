package org.sgine.property

import org.sgine.core.ProcessingMode

import org.sgine.event.EventHandler

import org.sgine.path.OPath
import org.sgine.path.PathChangeEvent

import scala.reflect.Manifest

class PathProperty[T](val path: OPath[T], overrideDefaultValue: T = null)(override implicit val manifest: Manifest[T]) extends MutableProperty[T] with ListenableProperty[T] {
	path.listeners += EventHandler(pathChanged, ProcessingMode.Blocking)
	if (overrideDefaultValue != null) {
		_defaultValue = overrideDefaultValue
	}
	
	override def apply() = path().getOrElse(defaultValue)
	
//	override def apply(value: T): Property[T] = throw new RuntimeException("PathProperty cannot be modified.")
	
	override def option() = path()
	
	private def pathChanged(evt: PathChangeEvent[T]) = changed(evt.oldValue, evt.newValue)
}