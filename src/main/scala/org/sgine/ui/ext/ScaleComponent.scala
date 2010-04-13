package org.sgine.ui.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

trait ScaleComponent extends MatrixComponent {
	val scale = new Scale(this)
	
	scale.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override def updateMatrix() = {
		super.updateMatrix()
		
		matrix().scale(scale.x(), scale.y(), scale.z())
	}
}