package org.sgine.render.spatial.renderer

import org.lwjgl.opengl.GL11._

import org.sgine.render.Face
import org.sgine.render.Material
import org.sgine.render.spatial.MeshData

trait LWJGLSpatialRenderer extends SpatialRenderer {
	protected[spatial] def render(mesh: MeshData) = {
		var face = mesh.cull match {
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
		
		if (mesh.material != null) {
			glEnable(GL_COLOR_MATERIAL)
			
			val m = mesh.material match {
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