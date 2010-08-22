package org.sgine.render.shape.renderer.lwjgl

import org.lwjgl.opengl.GL11._

import org.sgine.core._

import org.sgine.render.shape._
import org.sgine.render.shape.renderer.ShapeRenderer

trait LWJGLShapeRenderer extends ShapeRenderer {
	protected[shape] final def render(shape: Shape) = {
		var face = shape.cull match {
			case Face.None => GL_FRONT_AND_BACK
			case Face.Front => GL_FRONT
			case Face.Back => GL_BACK
			case Face.Both => GL_FRONT
			case _ => throw new RuntimeException("Unknown Face: " + shape.cull)
		}
		
		if (shape.material != null) {
			glEnable(GL_COLOR_MATERIAL)
			
			val m = shape.material match {
				case Material.Emission => GL_EMISSION
				case Material.Ambient => GL_AMBIENT
				case Material.Diffuse => GL_DIFFUSE
				case Material.Specular => GL_SPECULAR
				case Material.AmbientAndDiffuse => GL_AMBIENT_AND_DIFFUSE
				case _ => throw new RuntimeException("Unsupported: " + shape.material)
			}
			glColorMaterial(face, m)
		}
		
		glEnable(GL_CULL_FACE)
		if ((shape.cull == Face.None) || (shape.cull == Face.Front)) {
			glCullFace(GL_FRONT)
			renderInternal(shape)
		}
		if ((shape.cull == Face.None) || (shape.cull == Face.Back)) {
			glCullFace(GL_BACK)
			renderInternal(shape)
		}
	}
	
	protected[shape] def renderInternal(shape: Shape): Unit
}