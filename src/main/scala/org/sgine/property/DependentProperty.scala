package org.sgine.property

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.event.PropertyChangeEvent

trait DependentProperty[T] extends Property[T] {
	protected def dependency: Property[T]
	
	private val initialized = new AtomicBoolean(false)
	private var firstChanged = false
	private var modified = false
	
	abstract override def apply() = {
		val value = super.apply()
		
		if ((initialized == null) || (initialized.compareAndSet(false, true))) {
			dependency match {
				case lp: ListenableProperty[T] => lp.listeners += EventHandler(dependencyChanged, ProcessingMode.Blocking)
				case _ =>
			}
		}
		
		if ((modified) || (dependency == null)) {
			value
		} else {
			dependency()
		}
	}
	
	abstract override def apply(value: T): Property[T] = {
		super.apply(value)
		
		if (!firstChanged) {
			firstChanged = true
		} else {
			modified = true
		}
		
		this
	}
	
	private def dependencyChanged(evt: PropertyChangeEvent[T]) = {
//		if (!modified) {
//			this match {
//				case cp: ChangeableProperty[T] => cp.changed(false)
//				case _ =>
//			}
//		}
	}
}