package org.sgine.render

import org.sgine.math.Matrix4

import org.lwjgl.opengl.GL11._

import org.sgine.render.font.AngelCodeFont

class FPS private(frequency: Double, font: AngelCodeFont, matrix: Matrix4) extends Function0[Unit] {
	private var elapsed: Double = 0.0
	private var frames: Long = 0
	private var accurate: Int = 0
	
	def apply() = {
		val time = Renderer.time.get
		
		elapsed += time;
		frames += 1;
		if (elapsed > frequency) {
			accurate = (frames / elapsed).round.toInt
			elapsed = 0.0;
			
			if (font == null) println("FPS: " + accurate);
			frames = 0
		}
		
		if (font != null) {
			glLoadMatrix(matrix.buffer)
			
			font.drawString(accurate.toString, true)
		}
	}
}

object FPS {
	def apply(frequency: Double = 1.0, font: AngelCodeFont = null, matrix: Matrix4 = Matrix4().translate(x = -630.0, y = 470.0, z = -1200.0)) = new FPS(frequency, font, matrix);
}