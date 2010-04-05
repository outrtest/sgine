package org.sgine.ui.ext

import org.sgine.event.Event

import org.sgine.math.mutable.MatrixPropertyContainer

import org.sgine.property.MutableProperty

import org.sgine.scene.NodeContainer

import org.sgine.ui.Component

trait MatrixComponent extends Component {
	val revalidateMatrix = new MutableProperty[Boolean](true)
	
	abstract override def preRender() = {
		super.preRender()
		
		if (revalidateMatrix()) {
			updateMatrix()
		}
	}
	
	def invalidateMatrix(evt: Event = null) = {
		revalidateMatrix := true
	}
	
	protected def updateMatrix() = {
		revalidateMatrix := false
		
		matrix().identity()
		applyParentMatrix(parent)
	}
	
	private def applyParentMatrix(container: NodeContainer) {
		if (container != null) {
			container match {
				case mpc: MatrixPropertyContainer => matrix().set(mpc.matrix())			// Apply the first parent that has a matrix
				case _ => applyParentMatrix(container.parent)							// Keep working our way up
			}
		}
	}
}