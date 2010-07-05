package org.sgine.ui.ext

import org.sgine.core._

import org.sgine.bounding.BoundingObject

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.ui.Component

import org.lwjgl.opengl.GL11._

import simplex3d.math._
import simplex3d.math.doublem._

trait RotationComponent extends Component {
	val rotation = new Rotation(this)
	
	rotation.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override protected def updateLocalMatrix() = {
		super.updateLocalMatrix()
		
		if (rotation != null) {
			var x = 0.0
			var y = 0.0
			var z = 0.0
			
			if (rotation.x.align() != HorizontalAlignment.Center) {
				this match {
					case bo: BoundingObject => {
						if (rotation.x.align() == HorizontalAlignment.Left) {
							x -= bo.bounding().width / 2.0
						} else {
							x += bo.bounding().width / 2.0
						}
					}
					case _ => error("HorizontalAlignments other than Center for rotation are only allowed on BoundingObjects")
				}
			}
			if (rotation.y.align() != VerticalAlignment.Middle) {
				this match {
					case bo: BoundingObject => {
						if (rotation.y.align() == VerticalAlignment.Top) {
							y += bo.bounding().height / 2.0
						} else {
							y -= bo.bounding().height / 2.0
						}
					}
					case _ => error("VerticalAlignments other than Middle for rotation are only allowed on BoundingObjects")
				}
			}
			if (rotation.z.align() != DepthAlignment.Middle) {
				this match {
					case bo: BoundingObject => {
						if (rotation.z.align() == DepthAlignment.Front) {
							z -= bo.bounding().depth / 2.0
						} else {
							z += bo.bounding().depth / 2.0
						}
					}
					case _ => error("DepthAlignments other than Middle for rotation are only allowed on BoundingObjects")
				}
			}
			
			if ((x != 0.0) || (y != 0.0) || (z != 0.0)) {
				localMatrix() := localMatrix().translate(Vec3d(x, y, z))
			}
			
			import org.sgine.math.MathUtil._
			localMatrix() := localMatrix().concatenate(eulerMat(rotation.x(), rotation.y(), rotation.z()))
			
//			if (rotation.x() != 0.0) localMatrix() := localMatrix() rotateX(rotation.x())
//			if (rotation.y() != 0.0) localMatrix() := localMatrix().rotateY(rotation.y())
//			if (rotation.z() != 0.0) localMatrix() := localMatrix().rotateZ(rotation.z())
			
			if ((x != 0.0) || (y != 0.0) || (z != 0.0)) {
				localMatrix() := localMatrix().translate(Vec3d(-x, -y, -z))
			}
		}
	}
}