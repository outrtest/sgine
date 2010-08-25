package org.sgine.property

import java.util.concurrent._

import org.sgine.path.OPath

import org.sgine.property.bind.Binding
import org.sgine.property.bind.DirectBinding
import org.sgine.property.bind.PathBinding

trait BindingProperty[T] extends ChangeableProperty[T] {
	protected var bindings: List[Binding[T]] = Nil
	protected var pathBindings: List[PathBinding[T]] = Nil
	
	private implicit val directConverter = (t: T) => t
	
	def bind(p: BindingProperty[T]): Unit = bind(p, directConverter)
	
	def bind[O](p: BindingProperty[O], converter: (O) => T): Unit = {
		apply(converter(p()))		// Synchronize the values
		
		p.reverseBind(new DirectBinding(this, converter))
	}
	
	def reverseBind(b: Binding[T]) = {
		synchronized {
			bindings = b :: bindings
		}
	}
	
	def unbind[O](p: BindingProperty[O]) = {
		p.synchronized {
			p.bindings = p.bindings.filterNot(directUnbind)
		}
	}
	
	def reverseUnbind(b: Binding[T]) = {
		synchronized {
			bindings = bindings.filterNot(_ == b)
		}
	}
	
	def bindPath(path: OPath) = {
		synchronized {
			pathBindings = new PathBinding(this, path) :: pathBindings
		}
	}
	
	def unbindPath(path: OPath) = {
		synchronized {
			pathBindings = pathBindings.filterNot(pathUnbind(_, path))
		}
	}
	
	private val directUnbind = (b: Binding[_]) => b match {
		case db: DirectBinding[_, _] => db.property == this
		case _ => false
	}
	
	private val pathUnbind = (b: PathBinding[_], path: OPath) => {
		if (b.path == path) {
			b.disconnect()
			true
		} else {
			false
		}
	}
	
	abstract override def changed(oldValue: T, newValue: T): Unit = {
		super.changed(oldValue, newValue)
		
		for (b <- bindings) {
			b := newValue
		}
	}
}