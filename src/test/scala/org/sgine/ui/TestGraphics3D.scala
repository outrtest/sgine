package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Graphics3D._
import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import org.sgine.ui.ext._

import org.lwjgl.opengl._
import org.lwjgl.opengl.GL11._

import scala.math._

import gr.zdimensions.zshapes.viewer.font._
import gr.zdimensions.zshapes.viewer.renderer.ShapeRenderer

object TestGraphics3D {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene", 4, 8, 4, 4)
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(200, 150)
		
		scene += new TestGraphics3D()
		
		r.renderable := RenderableScene(scene)
	}
}

class TestGraphics3D extends AdvancedComponent {
	private lazy val caps = GLContext.getCapabilities()
	private lazy val r = ShapeRenderer.shapeRendererGetAA(caps)
	private lazy val fontMap = new FontMap("Z:/programming/classpath/zShapes/samples/test.zfm")
	private lazy val font = new Font(fontMap, 80.0f)
	private lazy val renderable = new TextArea(font, "Hello World!")
	lineWidth = 2.0
	
	def drawComponent() = {
		drawRect(0.0, 0.0, 100.0, 100.0)
		
		drawRoundRect(0.0, 115.0, 100.0, 100.0, 10)
		
		r.render(renderable)
	}
}