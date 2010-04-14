package org.sgine.ui

import org.sgine.event.Event

import org.sgine.ui.ext.MatrixComponent

trait CompositeComponent extends MatrixComponent {
	def children: List[Component]
	
	override protected def initComponent() = {
		for (c <- children) {
			c.parent = this
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
		for (c <- children) {
			c.render()
		}
	}
}