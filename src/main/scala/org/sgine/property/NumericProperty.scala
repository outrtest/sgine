package org.sgine.property

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.event.Event
import org.sgine.event.EventDelegate
import org.sgine.event.EventHandler
import org.sgine.event.Listenable
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.NumericPropertyChangeEvent

import org.sgine.work.Updatable

class NumericProperty(protected implicit val manifest: Manifest[Double]) extends Property[Double] with Listenable with Updatable {
	private var _name: String = _
	private var _dependency: Property[Double] = _
	
	lazy val name = determineName
	def dependency = _dependency
	
	private var value = 0.0
	
	// Dependent property
	private var initialized = new AtomicBoolean(false)
	private var firstChanged = false
	private var modified = false
	
	private var oldValue = 0.0
	
	private var _parent: Listenable = _
	
	override def parent = _parent
	
	protected def parent_=(_parent: Listenable) = this._parent = _parent
	
	protected var bindings: List[NumericProperty] = Nil
	
	var animator: Function3[Double, Double, Double, Double] = null
	
	private[property] var _target: Double = apply()
	
	def target = _target
	
	def this(value: Double) = {
		this()
		
		apply(value)
	}
	
	def this(value: Double, parent: Listenable) = {
		this(value)
		
		this.parent = parent
	}
	
	def this(value: Double, parent: Listenable, name: String) = {
		this(value, parent)
		
		_name = name
	}
	
	def this(value: Double, parent: Listenable, name: String, dependency: Property[Double]) = {
		this(value, parent, name)
		
		_dependency = dependency
	}
	
	def this(value: Double, parent: Listenable, name: String, dependency: Property[Double], listener: EventHandler) = {
		this(value, parent, name, dependency)

		listeners += listener
	}
	
	def +=(value: Double) = {
		apply(apply() + value)
	}
	
	def -=(value: Double) = {
		apply(apply() - value)
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
	
	def apply(): Double = {
		if ((initialized != null) && (dependency != null) && (initialized.compareAndSet(false, true))) {
			listeners += EventHandler(dependencyChanged, ProcessingMode.Blocking)
		}
		
		if ((modified) || (dependency == null)) {
			value
		} else {
			dependency()
		}
	}
	
	def apply(value: Double): Property[Double] = {
		initUpdatable()
		
		if (animator != null) {
			_target = value
		} else {
			apply(value, true)
		}
		
		this
	}
	
	def apply(value: Double, invokeListeners: Boolean): Property[Double] = {
		this.value = value
		
		if (!firstChanged) {
			firstChanged = true
		} else {
			modified = true
		}
		
		if (invokeListeners) {
			changed()
		}
		
		this
	}
	
	def set(value: Double): Property[Double] = {
		apply(value, true)
		_target = value
		
		if (animator != null) {
			animator(_target, _target, 1.0)
		}
		
		this
	}
	
	override def :=(value: Double) = apply(value)
	
	override def get() = apply()
	
	override def update(time: Double) = {
		super.update(time)
		
		if (animator != null) {
			val current = apply()
			
			if (current != _target) {
				val result = animator(current, _target, time)
				
				apply(result, true)
			}
		}
	}
	
	def isAnimating() = apply() != _target
	
	def waitForTarget() = {
		while (isAnimating) {
			Thread.sleep(10)
		}
	}
	
	def useDependency() = {
		modified = false
	}
	
	private def dependencyChanged(evt: NumericPropertyChangeEvent) = {
		if (!modified) {
			changed(false)
		}
	}
	
	def changed(equalityCheck: Boolean = true): Unit = {
		val newValue = apply()
		
		if ((!equalityCheck) || (newValue != oldValue)) {
			changed(oldValue, newValue)
		}
		
		oldValue = newValue
	}
	
	def changedLocal() = changed(this(), this())
	
	def changed(oldValue: Double, newValue: Double): Unit = {
		var adjusting = false
		if (target != newValue) {
			adjusting = true
		}
		
		for (b <- bindings) {
			b := newValue
		}
		
		Event.enqueue(NumericPropertyChangeEvent(this, oldValue, newValue, adjusting))
	}
	
	def bind(p: NumericProperty) = {
		apply(p())
		
		p.synchronized {
			p.bindings = this :: p.bindings
		}
	}
	
	def unbind(p: NumericProperty) = {
		p.synchronized {
			p.bindings = p.bindings.filterNot(_ == this)
		}
	}
	
	override def resolveElement(key: String) = key match {
		case "animator" => Some(animator)
		case "target" => Some(target)
		case "name" => Some(name)
		case "parent" => Some(parent)
		case _ => super.resolveElement(key)
	}
}