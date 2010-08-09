package org.sgine.ui.layout

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingBox

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.property.AdvancedProperty

import org.sgine.scene.NodeContainer
import org.sgine.scene.event.NodeContainerEvent

import org.sgine.work.Updatable

trait LayoutNode extends NodeContainer with Updatable {
	var layout = new AdvancedProperty[(NodeContainer) => Unit](Layout.default, this)
	
	private val revalidateLayout = new AtomicBoolean(true)
	
	listeners += EventHandler(boundingChanged, ProcessingMode.Blocking, Recursion.Children)
	listeners += EventHandler(childrenChanged)
	
	def invalidateLayout() = {
		revalidateLayout.set(true)
		
		initUpdatable()
	}
	
	private def boundingChanged(evt: BoundingChangeEvent) = {
		if (evt.listenable != this) {
			invalidateLayout()
		}
	}
	
	private def childrenChanged(evt: NodeContainerEvent) = {
		invalidateLayout()
	}
	
	abstract override def update(time: Double) = {
		super.update(time)
		
		if (revalidateLayout.compareAndSet(true, false)) {
			layout.option match {
				case Some(l) => l(this)
				case None => revalidateBounds()
			}
		}
	}
	
	private def revalidateBounds() = {
		// TODO: support
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
}