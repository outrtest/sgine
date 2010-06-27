package org.sgine.ui

import org.sgine.render.StandardDisplay

object TestDrawable extends StandardDisplay {
	def setup() = {
		val component = DrawableComponent()
		component.width := 250
		component.height := 250
		component.painter := testPaint
		scene += component
		
		component.invalidateCache()
	}
	
	def testPaint(g: java.awt.Graphics2D) = {
		g.setBackground(java.awt.Color.RED)
		g.setColor(java.awt.Color.ORANGE)
		g.clearRect(0, 0, 250, 250)
		g.fillRoundRect(50, 50, 150, 150, 15, 15)
		g.setColor(java.awt.Color.BLUE)
		g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 48))
		g.drawString("Test", 72, 135)
	}
}