package org.sgine.render.shape.renderer.lwjgl

import org.lwjgl.opengl.GL11._

import org.sgine.core._

import org.sgine.render.shape.ShapeData
import org.sgine.render.shape.renderer.ShapeRenderer

trait LWJGLShapeRenderer extends ShapeRenderer {
	protected[shape] final def render(data: ShapeData) = {
		var face = data.cull match {
			case Face.None => GL_FRONT_AND_BACK
			case Face.Front => GL_FRONT
			case Face.Back => GL_BACK
			case Face.Both => GL_FRONT
			case _ => throw new RuntimeException("Unknown Face: " + data.cull)
		}
		
		if (data.material != null) {
			glEnable(GL_COLOR_MATERIAL)
			
			val m = data.material match {
				case Material.Emission => GL_EMISSION
				case Material.Ambient => GL_AMBIENT
				case Material.Diffuse => GL_DIFFUSE
				case Material.Specular => GL_SPECULAR
				case Material.AmbientAndDiffuse => GL_AMBIENT_AND_DIFFUSE
				case _ => throw new RuntimeException("Unsupported: " + data.material)
			}
			glColorMaterial(face, m)
		}
		
		glEnable(GL_CULL_FACE)
		if ((data.cull == Face.None) || (data.cull == Face.Front)) {
			glCullFace(GL_FRONT)
			renderInternal(data)
		}
		if ((data.cull == Face.None) || (data.cull == Face.Back)) {
			glCullFace(GL_BACK)
			renderInternal(data)
		}
	}
	
	protected[shape] def renderInternal(data: ShapeData): Unit
}