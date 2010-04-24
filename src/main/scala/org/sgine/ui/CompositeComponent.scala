package org.sgine.ui

import org.sgine.event.Event

import org.sgine.scene.NodeContainer

import org.sgine.ui.ext.MatrixComponent

// TODO: should this remain a Component or should it just be a NodeContainer?
trait CompositeComponent extends MatrixComponent with NodeContainer {
	def children: List[Component]
	
	def iterator = children.iterator
	
	override protected def initComponent() = {
		for (c <- children) {
			c.parent = this
			
			c match {
				case mc: MatrixComponent => mc.invalidateMatrix()
				case _ =>
			}
		}
	}
	
	override def invalidateMatrix(evt: Event = null) = {
		val b = super.invalidateMatrix()
		
		for (c <- children) c match {
			case mc: MatrixComponent => mc.invalidateMatrix()
			case _ =>
		}
		
		b
	}
	
	def drawComponent() = {
		// Rendering of children handled by the scene
	}
}