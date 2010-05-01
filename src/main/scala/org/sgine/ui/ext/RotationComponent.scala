package org.sgine.ui.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.ui.Component

trait RotationComponent extends Component {
	val rotation = new Rotation(this)
	
	rotation.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override protected def updateLocalMatrix() = {
		super.updateLocalMatrix()
		
		if (rotation != null) {
			localMatrix().rotate(rotation.x(), rotation.y(), rotation.z())
		}
	}
}