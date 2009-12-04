package org.sgine.opengl

class FPS private() extends Function1[Double, Unit] {
	private var elapsed:Double = 0.0;
	private var frames:Long = 0;
	
	def apply(time:Double) = {
		elapsed += time;
		frames += 1;
		if (elapsed > 1.0) {
			elapsed = 0.0;
			println("FPS: " + frames);
			frames = 0
		}
	}
}

object FPS {
	def apply() = new FPS();
}