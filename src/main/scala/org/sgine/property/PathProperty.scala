package org.sgine.property

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.path.OPath
import org.sgine.path.PathChangeEvent

import scala.reflect.Manifest

class PathProperty[T](val path: OPath[T])(override protected implicit val manifest: Manifest[T]) extends MutableProperty[T] with ListenableProperty[T] {
	path.listeners += EventHandler(pathChanged, ProcessingMode.Blocking)
	
	override def apply() = path().getOrElse(null)
	
	override def apply(value: T): Property[T] = throw new RuntimeException("PathProperty cannot be modified.")
	
	private def pathChanged(evt: PathChangeEvent[T]) = changed(evt.oldValue, evt.newValue)
}