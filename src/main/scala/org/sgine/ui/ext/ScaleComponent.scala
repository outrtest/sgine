package org.sgine.ui.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.ui.Component

import simplex3d.math._
import simplex3d.math.doublem._

trait ScaleComponent extends Component {
	val scale = new Scale(this)
	
	scale.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override protected def updateLocalMatrix() = {
		super.updateLocalMatrix()
		
		if (scale != null) {
			localMatrix() := localMatrix().scale(Vec3d(scale.x(), scale.y(), scale.z()))
		}
	}
}