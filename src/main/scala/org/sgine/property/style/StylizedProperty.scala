package org.sgine.property.style

import java.lang.ref.WeakReference

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.core.ProcessingMode

import org.sgine.event.ChangeEvent
import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.property.ChangeableProperty
import org.sgine.property.Property

trait StylizedProperty[T] extends Property[T] {
	private var _style: Function0[T] = _
	
	private val initialized = new AtomicBoolean(false)
	private var firstChanged = false
	private var modified = false
	
	final def style = _style
	
	abstract override def apply() = {
		val value = super.apply()
		
		if ((initialized != null) && (_style != null) && (initialized.compareAndSet(false, true))) {
			_style match {
				case l: Listenable => l.listeners += EventHandler(styleChanged, ProcessingMode.Blocking)
				case _ =>
			}
		}
		
		if ((modified) || (_style == null)) {
			value
		} else {
			_style()
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
	
	def useStyle = !modified
	
	def useStyle_=(b: Boolean) = {
		if (modified == b) {
			modified = !b
			
			styleChanged(null)
		}
	}
	
	/**
	 * Invoked when the style is modified from the outside.
	 * 
	 * @param style
	 */
	protected[style] def changeStyle(style: Function0[T]) = {
		if (_style != style) {
			_style = style
			styleChanged(null)
		}
	}
	
	private def styleChanged(evt: ChangeEvent[T]) = {
		if (useStyle) {
			this match {
				case cp: ChangeableProperty[_] => cp.changed(false)
				case _ =>
			}
		}
	}
}