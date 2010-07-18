package org.sgine.render

import org.lwjgl.opengl.GL11._

import org.lwjgl.BufferUtils

import org.sgine.core.Color

import org.sgine.property.AdvancedProperty
import org.sgine.property.TransactionalProperty
import org.sgine.property.container.PropertyContainer

class Fog private[render](val renderer: Renderer) extends PropertyContainer {
	override val parent = renderer
	val enabled = new AdvancedProperty[Boolean](false, this) with TransactionalProperty[Boolean]
	val color = new AdvancedProperty[Color](Color.Black, this) with TransactionalProperty[Color]
	val start = new AdvancedProperty[Double](100.0, this) with TransactionalProperty[Double]
	val end = new AdvancedProperty[Double](2000.0, this) with TransactionalProperty[Double]
	val equation = new AdvancedProperty[FogEquation](FogEquation.Linear, this) with TransactionalProperty[FogEquation]
	val density = new AdvancedProperty[Double](0.5, this) with TransactionalProperty[Double]
	
	def update() = {
		if (enabled.uncommitted) {
			enabled.commit()
			enabled() match {
				case true => glEnable(GL_FOG)
				case false => glDisable(GL_FOG)
			}
		}
		if (color.uncommitted) {
			color.commit()
			val c = color()
			Fog.store4.clear()
			Fog.store4.put(c.red.toFloat)
			Fog.store4.put(c.green.toFloat)
			Fog.store4.put(c.blue.toFloat)
			Fog.store4.put(c.alpha.toFloat)
			Fog.store4.flip()
			glFog(GL_FOG_COLOR, Fog.store4)
		}
		if (start.uncommitted) {
			start.commit()
			glFogf(GL_FOG_START, start().toFloat)
		}
		if (end.uncommitted) {
			end.commit()
			glFogf(GL_FOG_END, end().toFloat)
		}
		if (equation.uncommitted) {
			equation.commit()
			val q = equation() match {
				case FogEquation.Linear => GL_LINEAR
				case FogEquation.Exp => GL_EXP
				case FogEquation.Exp2 => GL_EXP2
			}
			glFogi(GL_FOG_MODE, q)
		}
		if (density.uncommitted) {
			density.commit()
			glFogf(GL_FOG_DENSITY, density().toFloat)
		}
	}
}

object Fog {
	protected val store4 = BufferUtils.createFloatBuffer(4)
}