package org.sgine.render

import org.lwjgl.opengl.GL11._

import org.sgine.render.font.Font

import simplex3d.math._
import simplex3d.math.doublem._

class FPS private(frequency: Double, font: Font, matrix: Mat3x4d) extends Function0[Unit] {
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
			Renderer.loadMatrix(matrix)
			
			font.drawString(accurate.toString, true)
		}
	}
}

object FPS {
	def apply(frequency: Double = 1.0, font: Font = null, matrix: Mat3x4d = Mat3x4d.Identity.translate(Vec3d(630.0, 470.0, -500.0))) = new FPS(frequency, font, matrix);
}