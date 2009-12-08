package org.sgine.opengl.scene

import org.sgine.scene._
import org.sgine.event._
import org.sgine.math.mutable._
import org.sgine.property._

import java.util.concurrent.atomic._

trait GLNode extends Listenable with Node with Function1[Double, Unit] {
	val location = new Location(this)
	val matrix = new Transform()
	
	private[scene] val matrixDirty = new AtomicBoolean()
	
	// Set up listeners
	private val lcHandler = EventHandler(locationChanged, ProcessingMode.Blocking, Recursion.Children)
	location.x.listeners += lcHandler
	location.y.listeners += lcHandler
	location.z.listeners += lcHandler
	
	def apply(time: Double) = {
		location.x.update(time)
		location.y.update(time)
		location.z.update(time)
	}
	
	def invalidateMatrix() = {
		matrixDirty.set(true)
	}
	
	private[scene] def validateMatrix(): Boolean = {
		var dirty = matrixDirty.get()
		var parentMatrix: Matrix4 = null
		parent match {
			case p: GLNodeContainer => {
				if (p.validateMatrix()) {
					dirty = true
				}
				parentMatrix = p.matrix.world
			}
			case _ =>
		}
		
		if (dirty) {
			// Update local and world matrix
			matrixDirty.set(false)														// Reset dirty flag
			matrix.local.set(matrix.adjust)												// Set the local to represent the current adjust matrix
			matrix.local.translate(location.x(), location.y(), location.z())			// Translate local to represent location
			if (parentMatrix != null) {
				matrix.world.mult(parentMatrix, matrix.local)
			} else {
				matrix.world.set(matrix.local)
			}
		}
		dirty
	}
	
	private def locationChanged(evt: PropertyChangeEvent[Double]) = invalidateMatrix()
}

class Transform {
	val adjust = Matrix4()
	val local = Matrix4()
	val world = Matrix4()
}

class Location(node: GLNode) {
	val x = new AdvancedProperty[Double](0.0, node)
	val y = new AdvancedProperty[Double](0.0, node)
	val z = new AdvancedProperty[Double](0.0, node)
}