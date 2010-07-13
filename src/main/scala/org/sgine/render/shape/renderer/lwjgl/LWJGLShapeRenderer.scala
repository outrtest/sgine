package org.sgine.render.shape.renderer.lwjgl

import org.lwjgl.opengl.GL11._

import org.sgine.render.Face
import org.sgine.render.Material
import org.sgine.render.shape.ShapeData
import org.sgine.render.shape.renderer.ShapeRenderer

trait LWJGLShapeRenderer extends ShapeRenderer {
	protected[shape] def render(data: ShapeData) = {
		var face = data.cull match {
			case Face.None => {
				glDisable(GL_CULL_FACE)
				GL_FRONT
			}
			case Face.Front => {
				glCullFace(GL_FRONT)
				glEnable(GL_CULL_FACE)
				GL_FRONT
			}
			case Face.Back => {
				glCullFace(GL_BACK)
				glEnable(GL_CULL_FACE)
				GL_BACK
			}
			case Face.Both => {
				glCullFace(GL_FRONT_AND_BACK)
				glEnable(GL_CULL_FACE)
				GL_FRONT_AND_BACK
			}
		}
		
		if (data.material != null) {
			glEnable(GL_COLOR_MATERIAL)
			
			val m = data.material match {
				case Material.Emission => GL_EMISSION
				case Material.Ambient => GL_AMBIENT
				case Material.Diffuse => GL_DIFFUSE
				case Material.Specular => GL_SPECULAR
				case Material.AmbientAndDiffuse => GL_AMBIENT_AND_DIFFUSE
			}
			glColorMaterial(face, m)
		}
	}
}