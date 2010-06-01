package org.sgine.ui.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.ui.Component

trait ScaleComponent extends Component {
	val scale = new Scale(this)
	
	scale.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override protected def updateLocalMatrix() = {
		super.updateLocalMatrix()
		
		if (scale != null) {
			localMatrix().scale(scale.x(), scale.y(), scale.z())
		}
	}
}