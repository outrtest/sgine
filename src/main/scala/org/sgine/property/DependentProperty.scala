package org.sgine.property

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.core.ProcessingMode

import org.sgine.event.ChangeEvent
import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import scala.reflect.Manifest

/**
 * DependentProperty provides a dependency (Function0[T]) that is relied upon
 * until a specific value is assigned to the property. This allows a Property
 * to define hierarchical defaults.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait DependentProperty[T] extends Property[T] {
	protected def dependency: Function0[T]
	
	private val initialized = new AtomicBoolean(false)
	private var firstChanged = false
	private var modified = false
	
	abstract override def apply() = {
		val value = super.apply()
		
		if ((initialized != null) && (dependency != null) && (initialized.compareAndSet(false, true))) {
			dependency match {
				case l: Listenable => l.listeners += EventHandler(dependencyChanged, ProcessingMode.Blocking)
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
	
	def useDependency = !modified
	
	def useDependency_=(b: Boolean) = {
		modified = !b
	}
	
	private def dependencyChanged(evt: ChangeEvent[T]) = {
		if (!modified) {
			this match {
				case cp: ChangeableProperty[_] => cp.changed(false)
				case _ =>
			}
		}
	}
}