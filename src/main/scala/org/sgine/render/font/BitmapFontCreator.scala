package org.sgine.render.font

import java.awt.{Font => AWTFont}
import java.awt.RenderingHints
import java.awt.font.FontRenderContext

import org.sgine.util.GeneralReusableGraphic

import scala.math._

// char id=97   x=204     y=107     width=17     height=19     xoffset=1     yoffset=13    xadvance=17     page=0  chnl=0
object BitmapFontCreator {
	def apply(font: AWTFont) = {
		val context = new FontRenderContext(null, true, true)
		val lm = font.getLineMetrics("a", context)
		val r = font.getStringBounds("a", context)
		println(lm.getAscent)
		println(lm.getBaselineIndex)
		println(lm.getDescent)
		println(lm.getHeight)
		println(lm.getLeading)
		println(r.getX + ", " + r.getCenterX + ", " + r.getMinX + ", " + r.getMaxX + ", " + r.getWidth)
		
		val rg = GeneralReusableGraphic
		val g = rg(800, 600)
		g.setFont(font)
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		g.setBackground(java.awt.Color.BLACK)
		g.clearRect(0, 0, 800, 600)
		g.setColor(java.awt.Color.WHITE)
		g.drawString("a", r.getCenterX.toFloat, lm.getHeight)
		
		val frame = new javax.swing.JFrame()
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE)
		frame.setSize(800, 600)
		val icon = new javax.swing.ImageIcon(rg())
		val label = new javax.swing.JLabel(icon)
		frame.setContentPane(label)
		frame.setVisible(true)
	}
	
	def main(args: Array[String]): Unit = {
		apply(new AWTFont("Arial", AWTFont.PLAIN, 32))
	}
}