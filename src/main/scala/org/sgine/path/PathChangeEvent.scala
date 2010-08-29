package org.sgine.path

import org.sgine.event.BasicEvent
import org.sgine.event.Listenable

import scala.reflect.Manifest

class PathChangeEvent[T](val path: OPath[T], val oldValue: Any, val newValue: Any)(implicit manifest: Manifest[T]) extends BasicEvent(path)