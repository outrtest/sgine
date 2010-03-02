package org.sgine.opengl.scene

import org.sgine.scene._
import org.sgine.event._
import org.sgine.math.mutable._
import org.sgine.property._
import org.sgine.property.container._
import org.sgine.property.event._

import java.util.concurrent.atomic._

trait GLNode extends Listenable with Node with Function1[Double, Unit] {
	val location = new Location(this)
	val rotation = new Rotation(this)
	val scale = new Scale(this)
	val matrix = new Transform()
	val alpha = new AdvancedProperty[Double](1.0)
	
	private[scene] val matrixDirty = new AtomicBoolean()
	private[scene] val alphaDirty = new AtomicBoolean()
	
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
	
	alpha.listeners += EventHandler(alphaChanged, ProcessingMode.Blocking, Recursion.Children)
	
	def apply(time: Double) = {
		alpha.update(time)
		location.update(time)
		rotation.update(time)
		scale.update(time)
	}
	
	def invalidateMatrix() = {
		matrixDirty.set(true)
	}
	
	def invalidateAlpha() = {
		alphaDirty.set(true)
	}
	
	private[scene] def validateMatrix(): Unit = {
		if (matrixDirty.get()) {						// Only update matrix if dirty
			matrixDirty.set(false)						// reset dirty flag
			var parentMatrix: Matrix4 = null
			parent match {								// make sure parent is validated
				case p: GLNodeContainer => {
					p.validateMatrix()
					parentMatrix = p.matrix.world
				}
				case _ =>
			}
			matrix.local.set(matrix.adjust)												// Set the local to represent the current adjust matrix
			matrix.local.translate(location.x(), location.y(), location.z())			// Translate local to represent location
			matrix.local.rotate(rotation.x() * Rotation.Radians, rotation.y() * Rotation.Radians, rotation.z() * Rotation.Radians)				// Rotate local to represent rotation
			matrix.local.scale(scale.x(), scale.y(), scale.z())							// Scale local to represent scale
			if (parentMatrix != null) {
				matrix.world.mult(parentMatrix, matrix.local)
			} else {
				matrix.world.set(matrix.local)
			}
		}
	}
	
	private[scene] def validateAlpha(): Unit = {
		if (alphaDirty.get()) {
			alphaDirty.set(false)
			var parentAlpha: Double = 1.0
			parent match {
				case p: GLNodeContainer => {
					p.validateAlpha()
					parentAlpha = p.matrix.alpha
				}
				case _ =>
			}
			matrix.alpha = alpha() * parentAlpha
		}
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
	
	private def alphaChanged(evt: PropertyChangeEvent[Double]) = invalidateAlpha()
}

class Transform {
	val adjust = Matrix4()
	val local = Matrix4()
	val world = Matrix4()
	var alpha = 1.0
}

class Location(node: GLNode) extends PropertyContainer {
	override val parent = node
	
	val x = new AdvancedProperty[Double](0.0, node)
	val y = new AdvancedProperty[Double](0.0, node)
	val z = new AdvancedProperty[Double](0.0, node)
}

class Rotation(node: GLNode) extends PropertyContainer {
	override val parent = node
	
	val x = new AdvancedProperty[Double](0.0, node)
	val y = new AdvancedProperty[Double](0.0, node)
	val z = new AdvancedProperty[Double](0.0, node)
}

object Rotation {
	val Radians = Math.Pi * 2.0
}

class Scale(node: GLNode) extends PropertyContainer {
	override val parent = node
	
	val x = new AdvancedProperty[Double](1.0, node)
	val y = new AdvancedProperty[Double](1.0, node)
	val z = new AdvancedProperty[Double](1.0, node)
}