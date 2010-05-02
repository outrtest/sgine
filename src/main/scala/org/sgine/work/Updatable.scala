package org.sgine.work

import java.lang.ref.WeakReference

/**
 * Updatable provides a simple trait that can be mixed-in to
 * provide asynchronous updates via a long-running job in
 * WorkManager.
 * 
 * All updatables are managed within a single thread, so the
 * update(Double) method not do any blocking work that may
 * cause pausing in the updating thread.
 * 
 * The only requirement to make this work is to simply mix
 * it in and provide an implementation of update(Double).
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Updatable {
	private var initializedUpdatable = false
	
	def initUpdatable() = {
		if (!initializedUpdatable) {
			initializedUpdatable = true
			
			Updatable.add(this)
		}
	}
	
	def update(time: Double): Unit = {
	}
}

object Updatable extends Function0[Unit] {
	var useWorkManager = false
	var workManager = DefaultWorkManager
	
	private var list: List[WeakReference[Updatable]] = Nil
	
	private var initialized = false
	
	/**
	 * The number of times per second all updatables will be updated.
	 * 
	 * Defaults to 60.
	 */
	var Rate = 60.0
	
	private def initialize() = {
		synchronized {
			if (!initialized) {
				if (useWorkManager) {
					initialized = true
					workManager += this
				}
			}
		}
	}
	
	def apply() = {
		while (true) {
			val shouldWait = update()
			
			if (shouldWait > 0) {
//				println("Should wait: " + shouldWait + " - " + shouldElapse)
				Thread.sleep(shouldWait)
			}
		}
	}
	
	var lastTime: Long = System.nanoTime
	
	def update() = {
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
		
		shouldElapse - elapsed
	}
	
	private def add(u: Updatable) = {
		initialize()
		
		synchronized {
			list = new WeakReference(u) :: list
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