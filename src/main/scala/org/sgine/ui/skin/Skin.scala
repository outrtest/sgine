package org.sgine.ui.skin

import org.sgine.core.ProcessingMode

import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.ui.SkinnedComponent

/**
 * Skin is a light-weight trait for use with
 * SkinnableComponent to draw a background skin
 * on a Component. A Skin instance may be assigned
 * to one and only one SkinnableComponent.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Skin extends PropertyContainer {
	protected var component: SkinnedComponent = _
	override def parent = component
	
	private val resizeHandler = EventHandler(resizedEvent, ProcessingMode.Blocking)
	
	/**
	 * Invoked upon assignment to a SkinnedComponent.
	 * 
	 * @param component
	 */
	protected[ui] def connect(component: SkinnedComponent) = {
		this.component = component
		
		Listenable.listenTo(resizeHandler, component.size.actual.width, component.size.actual.height)
		
		resized()
	}
	
	/**
	 * Invoked upon removal from a SkinnedComponent.
	 * 
	 * @param component
	 */
	protected[ui] def disconnect(component: SkinnedComponent) = {
		this.component = null
		
		component.size.actual.width.listeners -= resizeHandler
		component.size.actual.height.listeners -= resizeHandler
	}
	
	/**
	 * Called in the render thread to draw to the screen.
	 */
	def draw(): Unit
	
	/**
	 * Called when the connected SkinnedComponent has been resized.
	 */
	def resized(): Unit
	
	private def resizedEvent(evt: PropertyChangeEvent[_]) = {
		resized()
	}
}