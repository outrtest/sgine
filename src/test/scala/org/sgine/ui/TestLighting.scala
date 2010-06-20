package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Graphics3D._
import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import org.sgine.ui.ext._

import org.lwjgl._
import org.lwjgl.opengl._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._

import scala.math._

object TestLighting {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Lighting", 4, 8, 4, 4)
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		val component = new Image()
		component.source := Resource("puppies.jpg")
		scene += component
		
		scene += new TestLighting()
		
		r.renderable := RenderableScene(scene)
	}
}

class TestLighting extends AdvancedComponent {
	lazy val initialized = initialize()
	
	def drawComponent() = {
		initialized
	}
	
	private def initialize() = {
		val lightAmbient = toFloatBuffer(0.2f, 0.3f, 0.6f, 1.0f)
		val lightDiffuse = toFloatBuffer(0.2f, 0.3f, 0.6f, 1.0f)
		val matAmbient = toFloatBuffer(0.6f, 0.6f, 0.6f, 1.0f)
		val matDiffuse = toFloatBuffer(0.6f, 0.6f, 0.6f, 1.0f)
		
		glEnable(GL_LIGHTING)
		glEnable(GL_LIGHT0)
		
		glMaterial(GL_FRONT_AND_BACK, GL_AMBIENT, matAmbient)
		glMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE, matDiffuse)
		
		glLight(GL_LIGHT0, GL_AMBIENT, lightAmbient)
		glLight(GL_LIGHT0, GL_DIFFUSE, lightDiffuse)
		
		glEnable(GL_DEPTH_TEST)
		glDepthFunc(GL_LEQUAL)
		
		glEnable(GL_CULL_FACE)
		glShadeModel(GL_SMOOTH)
		
		glEnable(GL_RESCALE_NORMAL)
	}
	
	private def toFloatBuffer(args: Float*) = {
		val buffer = BufferUtils.createFloatBuffer(args.length)
		
		for (f <- args) {
			buffer.put(f)
		}
		buffer.flip()
		
		buffer
	}
}