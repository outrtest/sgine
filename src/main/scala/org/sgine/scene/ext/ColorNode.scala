package org.sgine.scene.ext

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.core.Color
import org.sgine.core.ProcessingMode
import org.sgine.core.mutable.{Color => MutableColor}

import org.sgine.event.EventHandler

import org.sgine.property.AdvancedProperty
import org.sgine.property.ImmutableProperty
import org.sgine.property.ModifiableProperty
import org.sgine.property.MutableProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer

import org.sgine.work.Updatable

trait ColorNode extends Node with Updatable {
	val color = new AdvancedProperty[Color](Color.White, this) with ModifiableProperty[Color]
	val alpha = new AdvancedProperty[Double](1.0, this)
	val worldColor = new ImmutableProperty[MutableColor](MutableColor(Color.White))
	
	private val storeColor = MutableColor()
	
	color.listeners += EventHandler(invalidateColor, ProcessingMode.Blocking)
	alpha.listeners += EventHandler(invalidateColor, ProcessingMode.Blocking)
	
	private val revalidateColor = new AtomicBoolean(true)
	
	def invalidateColor(evt: PropertyChangeEvent[Color] = null) = {
		revalidateColor.set(true)
		
		initUpdatable()
	}
	
	abstract override def update(time: Double) = {
		super.update(time)
		
		if ((revalidateColor != null) && (revalidateColor.compareAndSet(true, false))) {
			refreshWorldColor()
		}
	}
	
	protected def refreshWorldColor() = {
		// Update to parent world color
		getParentWorldColor(parent) match {
			case c: Color => storeColor(c)
			case _ => storeColor(Color.White)
		}
		
		// Update store
		storeColor.red = storeColor.red * color().red
		storeColor.green = storeColor.green * color().green
		storeColor.blue = storeColor.blue * color().blue
		storeColor.alpha = storeColor.alpha * color().alpha * alpha()
		
		// Update world color
		worldColor()(storeColor)
		
		// Invalidate children if NodeContainer
		invalidateChildren(this)
	}
	
	protected def getParentWorldColor(parent: Node): Color = {
		parent match {
			case null => null
			case cn: ColorNode => cn.worldColor()
			case _ => getParentWorldColor(parent.parent)
		}
	}
	
	private def invalidateChildren(n: Node): Unit = {
		n match {
			case container: NodeContainer => {
				for (c <- container.children) c match {
					case cn: ColorNode => cn.invalidateColor()
					case _ => invalidateChildren(c)
				}
			}
			case _ => // Not a container, so no children
		}
	}
	
	def isTransparent = worldColor().alpha == 0.0
	
	def isTranslucent = (worldColor().alpha > 0.0) && (worldColor().alpha < 1.0)
	
	def isOpaque = worldColor().alpha == 1.0
}