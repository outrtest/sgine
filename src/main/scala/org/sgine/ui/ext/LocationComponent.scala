package org.sgine.ui.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.property.event.PropertyChangeEvent

trait LocationComponent extends MatrixComponent {
	val location = new Location(this)
	
	location.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override def updateMatrix() = {
		super.updateMatrix()
		
		matrix().translate(location.x(), location.y(), location.z())
	}
}