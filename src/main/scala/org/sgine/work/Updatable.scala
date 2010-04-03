package org.sgine.work

import java.lang.ref.WeakReference

trait Updatable {
	def update(time: Double): Unit
	
	Updatable.add(this)
}

object Updatable extends Function0[Unit] {
	private var list: List[WeakReference[Updatable]] = Nil
	
	var workManager: WorkManager = DefaultWorkManager
	
	lazy val initted = initialize()
	
	/**
	 * The number of times per second all updatables will be updated.
	 * 
	 * Defaults to 60.
	 */
	var Rate = 60.0
	
	private def initialize() = {
		workManager += this
		true
	}
	
	def apply() = {
		var lastTime: Long = System.nanoTime
		while (true) {
			val time = System.nanoTime
			val frequency = (time - lastTime) / 1000000000.0
			lastTime = time
			val shouldElapse = Math.round(1000.0 / Rate)
			val l = list
			for (wr <- l) {
				val u = wr.get
				if (u == null) {
					list -= wr
				} else {
					u.update(frequency)
				}
			}
			val elapsed = (System.nanoTime - time) / 1000000
			val shouldWait = shouldElapse - elapsed
			if (shouldWait > 0) {
//				println("Should wait: " + shouldWait + " - " + shouldElapse)
				Thread.sleep(shouldWait)
			} else {
//				println("Updatable is updating slower than it should by: " + shouldWait)
			}
		}
	}
	
	private def add(u: Updatable) = {
		if (initted) {
			synchronized {
				list = new WeakReference(u) :: list
			}
		}
	}
	
	private def remove(u: Updatable) = {
		synchronized {
			list.find(wr => wr.get == u) match {
				case s: Some[WeakReference[Updatable]] => list -= s.get
				case None =>
			}
		}
	}
}