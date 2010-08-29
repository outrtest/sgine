package org.sgine.property.bind

import org.sgine.event._

import org.sgine.path._

import org.sgine.property.BindingProperty

import scala.reflect.Manifest

class PathBinding[T](val property: BindingProperty[T], val path: OPath[T])(implicit manifest: Manifest[T]) extends Binding[T] {
	private var reverse: BindingProperty[T] = _
	
	updateBinding()
	path.listeners += EventHandler(updateBinding, ProcessingMode.Blocking)
	
	def :=(value: T) = property match {
		case null =>
		case p => p := value
	}
	
	private def updateBinding(evt: PathChangeEvent[T] = null) = {
		val p = path() match {
			case Some(s) => s.asInstanceOf[BindingProperty[T]]
			case None => null
		}
		
		if (p != reverse) {
			if (reverse != null) {
				reverse.reverseUnbind(this)
			}
			if (p != null) {
				property := p()			// Make sure the value is the same
				p.reverseBind(this)
			}
			
			reverse = p
		}
	}
	
	def disconnect() = {
		if (reverse != null) {
			reverse.reverseUnbind(this)
		}
	}
}