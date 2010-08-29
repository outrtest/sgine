package org.sgine.ui

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingBox

import org.sgine.event._

import org.sgine.property._
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.event.NodeContainerEvent

import org.sgine.ui.layout._

import org.sgine.work.Updatable

class Container extends GeneralNodeContainer with Component with Updatable {
	val layout = new AdvancedProperty[(Container) => Unit](Layout.default, this)
	
	private val revalidateLayout = new AtomicBoolean(true)
	
	bounding.listeners += EventHandler(boundingChanged, ProcessingMode.Blocking, Recursion.Children)
	listeners += EventHandler(childrenChanged)
	
	def invalidateLayout() = {
		revalidateLayout.set(true)
		
		initUpdatable()
	}
	
	private def boundingChanged(evt: PropertyChangeEvent[_]) = {
		invalidateLayout()
	}
	
	private def childrenChanged(evt: NodeContainerEvent) = {
		invalidateLayout()
	}
	
	override def update(time: Double) = {
		super.update(time)
		
		if (revalidateLayout.compareAndSet(true, false)) {
			layout.option match {
				case Some(l) => l(this)
				case None => revalidateBounds()
			}
		}
	}
	
	private def revalidateBounds() = {
		// TODO: support non-layout bounds update
//		var width, height, depth = 0.0
//		for (c <- this) c match {
//			case bo: BoundingObject => {
//				width = max()
//			}
//		}
//		this match {
//			case bo: BoundingObject => bo.bounding() match {
//				case bb: BoundingBox => {
//					bb.width = width
//					bb.height = height
//					bb.depth = depth
//					
//					val e = new BoundingChangeEvent(bo, bb)
//					Event.enqueue(e)
//				}
//				case _ =>
//			}
//			case _ =>
//		}
	}
	
	def drawComponent() = {
	}
}