package org.sgine.opengl.scene

import org.sgine.scene._
import org.sgine.event._
import org.sgine.math.mutable._
import org.sgine.property._

import java.util.concurrent.atomic._

trait GLNode extends Listenable with Node with Function1[Double, Unit] {
	val location = new Location(this)
	val rotation = new Rotation(this)
	val scale = new Scale(this)
	val matrix = new Transform()
	
	private[scene] val matrixDirty = new AtomicBoolean()
	
	// Set up listeners
	private val lcHandler = EventHandler(locationChanged, ProcessingMode.Blocking, Recursion.Children)
	location.x.listeners += lcHandler
	location.y.listeners += lcHandler
	location.z.listeners += lcHandler
	rotation.x.listeners += lcHandler
	rotation.y.listeners += lcHandler
	rotation.z.listeners += lcHandler
	scale.x.listeners += lcHandler
	scale.y.listeners += lcHandler
	scale.z.listeners += lcHandler
	
	def apply(time: Double) = {
		location.x.update(time)
		location.y.update(time)
		location.z.update(time)
		
		rotation.x.update(time)
		rotation.y.update(time)
		rotation.z.update(time)
		
		scale.x.update(time)
		scale.y.update(time)
		scale.z.update(time)
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
			matrix.local.rotate(rotation.x() * Rotation.Radians, rotation.y() * Rotation.Radians, rotation.z() * Rotation.Radians)				// Rotate local to represent rotation
			matrix.local.translate(location.x(), location.y(), location.z())			// Translate local to represent location
			matrix.local.scale(scale.x(), scale.y(), scale.z())							// Scale local to represent scale
			if (parentMatrix != null) {
				matrix.world.mult(parentMatrix, matrix.local)
			} else {
				matrix.world.set(matrix.local)
			}
		}
		dirty
	}
	
	def translateResolution(width: Double, height: Double) = {
		val m = matrix.adjust
		m.identity()
		m.translateZ(-1.21)
		m.scale(1.0 / height)
		m.translateX(-(width / 2.0))
		m.translateY(height / 2.0)
		
		invalidateMatrix()
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

class Rotation(node: GLNode) {
	val x = new AdvancedProperty[Double](0.0, node)
	val y = new AdvancedProperty[Double](0.0, node)
	val z = new AdvancedProperty[Double](0.0, node)
}

object Rotation {
	val Radians = Math.Pi * 2.0
}

class Scale(node: GLNode) {
	val x = new AdvancedProperty[Double](1.0, node)
	val y = new AdvancedProperty[Double](1.0, node)
	val z = new AdvancedProperty[Double](1.0, node)
}