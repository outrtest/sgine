package org.sgine.ui.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.property.event.PropertyChangeEvent

import org.sgine.ui.Component

trait LocationComponent extends Component {
	val location = new Location(this)
	
	location.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override protected def updateLocalMatrix() = {
		super.updateLocalMatrix()
		
		if (location != null) {
			localMatrix().translate(location.x(), location.y(), location.z())
		}
	}
}