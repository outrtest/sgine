package org.sgine.ui.ext

import org.sgine.bounding.BoundingObject

import org.sgine.core.DepthAlignment
import org.sgine.core.HorizontalAlignment
import org.sgine.core.VerticalAlignment

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.property.NumericProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.ui.Component

import simplex3d.math._
import simplex3d.math.doublem._

trait LocationComponent extends Component {
	val location = new Location(this)
	
	location.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override protected def updateLocalMatrix() = {
		super.updateLocalMatrix()
		
		if (location != null) {
			var x = location.x()
			var y = location.y()
			var z = location.z()
			
			if (location.x.align() != HorizontalAlignment.Center) {
				this match {
					case bo: BoundingObject => {
						if (location.x.align() == HorizontalAlignment.Left) {
							x += bo.bounding().width / 2.0
						} else {
							x -= bo.bounding().width / 2.0
						}
					}
					case _ => error("HorizontalAlignments other than Center are only allowed on BoundingObjects")
				}
			}
			if (location.y.align() != VerticalAlignment.Middle) {
				this match {
					case bo: BoundingObject => {
						if (location.y.align() == VerticalAlignment.Top) {
							y -= bo.bounding().height / 2.0
						} else {
							y += bo.bounding().height / 2.0
						}
					}
					case _ => error("VerticalAlignments other than Middle are only allowed on BoundingObjects")
				}
			}
			if (location.z.align() != DepthAlignment.Middle) {
				this match {
					case bo: BoundingObject => {
						if (location.z.align() == DepthAlignment.Front) {
							z += bo.bounding().depth / 2.0
						} else {
							z -= bo.bounding().depth / 2.0
						}
					}
					case _ => error("DepthAlignments other than Middle are only allowed on BoundingObjects")
				}
			}
			
			localMatrix() := localMatrix().translate(Vec3d(x, y, z))
		}
	}
}