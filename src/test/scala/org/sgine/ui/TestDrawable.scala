package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

object TestDrawable {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Drawable")
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		val component = DrawableComponent()
		component.width := 250
		component.height := 250
		component.painter := testPaint
		scene += component
		
		component.invalidateCache()
		
		r.renderable := RenderableScene(scene)
	}
	
	def testPaint(g: java.awt.Graphics2D) = {
		println("DRAWING!")
		g.setBackground(java.awt.Color.RED)
		g.setColor(java.awt.Color.ORANGE)
		g.clearRect(0, 0, 250, 250)
		g.fillRoundRect(50, 50, 150, 150, 15, 15)
		g.setColor(java.awt.Color.BLUE)
		g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 48))
		g.drawString("Test", 72, 135)
	}
}