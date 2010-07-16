package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Graphics3D._
import org.sgine.render.Renderer
import org.sgine.render.RenderSettings
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import org.sgine.ui.ext._

import org.lwjgl.opengl._
import org.lwjgl.opengl.GL11._

import scala.math._

object TestGraphics3D {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test RenderScene", RenderSettings.High)
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		scene += new TestGraphics3D()
		
		r.renderable := RenderableScene(scene)
	}
}

class TestGraphics3D extends AdvancedComponent {
	def drawComponent() = {
		drawRect(0.0, 0.0, 100.0, 100.0)
		
		drawRoundRect(0.0, 115.0, 100.0, 100.0, 10)
	}
}