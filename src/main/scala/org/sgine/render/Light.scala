package org.sgine.render

import org.lwjgl._
import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.property.AdvancedProperty
import org.sgine.property.TransactionalProperty
import org.sgine.property.container.PropertyContainer

import simplex3d.math.doublem._

class Light private[render](val index: Int, val renderer: Renderer) extends PropertyContainer {
	private val glLightIndex = getLight()
	
	override val parent = renderer
	val enabled = new AdvancedProperty[Boolean](false, this) with TransactionalProperty[Boolean]
	val ambience = new AdvancedProperty[Color](Color.Black, this) with TransactionalProperty[Color]
	val diffuse = new AdvancedProperty[Color](if (index == 0) Color.White else Color.Black, this) with TransactionalProperty[Color]
	val specular = new AdvancedProperty[Color](if (index == 0) Color.White else Color.Black, this) with TransactionalProperty[Color]
	val position = new AdvancedProperty[Vec4d](Vec4d(0.0, 0.0, 1.0, 0.0), this) with TransactionalProperty[Vec4d]
	val spotDirection = new AdvancedProperty[Vec3d](Vec3d(0.0, 0.0, -1.0), this) with TransactionalProperty[Vec3d]
	val spotExponent = new AdvancedProperty[Int](0, this) with TransactionalProperty[Int]
	val spotCutoff = new AdvancedProperty[Int](180, this) with TransactionalProperty[Int]
	val constantAttenuation = new AdvancedProperty[Double](1.0, this) with TransactionalProperty[Double]
	val linearAttenuation = new AdvancedProperty[Double](0.0, this) with TransactionalProperty[Double]
	val quadraticAttenuation = new AdvancedProperty[Double](0.0, this) with TransactionalProperty[Double]
	
	def update() = {
		if (enabled.uncommitted) {
			renderer.updateLighting()
			
			enabled.commit()
			enabled() match {
				case true => glEnable(glLightIndex)
				case false => glDisable(glLightIndex)
			}
		}
		if (ambience.uncommitted) {
			ambience.commit()
			val c = ambience()
			Light.store4.clear()
			Light.store4.put(c.red.toFloat)
			Light.store4.put(c.green.toFloat)
			Light.store4.put(c.blue.toFloat)
			Light.store4.put(c.alpha.toFloat)
			Light.store4.flip()
			glLight(glLightIndex, GL_AMBIENT, Light.store4)
		}
		if (diffuse.uncommitted) {
			diffuse.commit()
			val c = diffuse()
			Light.store4.clear()
			Light.store4.put(c.red.toFloat)
			Light.store4.put(c.green.toFloat)
			Light.store4.put(c.blue.toFloat)
			Light.store4.put(c.alpha.toFloat)
			Light.store4.flip()
			glLight(glLightIndex, GL_DIFFUSE, Light.store4)
		}
		if (specular.uncommitted) {
			specular.commit()
			val c = specular()
			Light.store4.clear()
			Light.store4.put(c.red.toFloat)
			Light.store4.put(c.green.toFloat)
			Light.store4.put(c.blue.toFloat)
			Light.store4.put(c.alpha.toFloat)
			Light.store4.flip()
			glLight(glLightIndex, GL_SPECULAR, Light.store4)
		}
		if (position.uncommitted) {
			position.commit()
			val v = position()
			Light.store4.clear()
			Light.store4.put(v.x.toFloat)
			Light.store4.put(v.y.toFloat)
			Light.store4.put(v.z.toFloat)
			Light.store4.put(v.w.toFloat)
			Light.store4.flip()
			glLight(glLightIndex, GL_POSITION, Light.store4)
		}
		if (spotDirection.uncommitted) {
			spotDirection.commit()
			val v = spotDirection()
			Light.store3.clear()
			Light.store3.put(v.x.toFloat)
			Light.store3.put(v.y.toFloat)
			Light.store3.put(v.z.toFloat)
			Light.store3.flip()
			glLight(glLightIndex, GL_SPOT_DIRECTION, Light.store3)
		}
		if (spotExponent.uncommitted) {
			spotExponent.commit()
			glLighti(glLightIndex, GL_SPOT_EXPONENT, spotExponent())
		}
		if (spotCutoff.uncommitted) {
			spotCutoff.commit()
			glLighti(glLightIndex, GL_SPOT_CUTOFF, spotCutoff())
		}
		if (constantAttenuation.uncommitted) {
			constantAttenuation.commit()
			glLightf(glLightIndex, GL_CONSTANT_ATTENUATION, constantAttenuation().toFloat)
		}
		if (linearAttenuation.uncommitted) {
			linearAttenuation.commit()
			glLightf(glLightIndex, GL_LINEAR_ATTENUATION, linearAttenuation().toFloat)
		}
		if (quadraticAttenuation.uncommitted) {
			quadraticAttenuation.commit()
			glLightf(glLightIndex, GL_QUADRATIC_ATTENUATION, quadraticAttenuation().toFloat)
		}
	}
	
	private def getLight() = index match {
		case 0 => GL_LIGHT0
		case 1 => GL_LIGHT1
		case 2 => GL_LIGHT2
		case 3 => GL_LIGHT3
		case 4 => GL_LIGHT4
		case 5 => GL_LIGHT5
		case 6 => GL_LIGHT6
		case 7 => GL_LIGHT7
		case _ => throw new RuntimeException("Invalid light index!")
	}
}

object Light {
	protected val store3 = BufferUtils.createFloatBuffer(3)
	protected val store4 = BufferUtils.createFloatBuffer(4)
}