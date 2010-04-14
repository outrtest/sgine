package org.sgine.ui.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

trait RotationComponent extends MatrixComponent {
	val rotation = new Rotation(this)
	
	rotation.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override def updateMatrix() = {
		super.updateMatrix()
		
		matrix().rotate(rotation.x(), rotation.y(), rotation.z())
	}
}