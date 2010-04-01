package org.sgine.render

class FPS private(frequency: Double) extends Function0[Unit] {
	private var elapsed: Double = 0.0
	private var frames: Long = 0
	
	def apply() = {
		val time = Renderer.time.get
		
		elapsed += time;
		frames += 1;
		if (elapsed > frequency) {
			val accurate = (frames / elapsed).round.toInt
			elapsed = 0.0;
			
			println("FPS: " + accurate);
			frames = 0
		}
	}
}

object FPS {
	def apply(frequency: Double = 1.0) = new FPS(frequency);
}