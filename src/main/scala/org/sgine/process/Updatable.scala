package org.sgine.process

import java.lang.ref.WeakReference

import org.sgine.util.FunctionRunnable

import scala.math._

trait Updatable {
	@volatile private var _elapsed = 0.0
	
	def elapsed = _elapsed
	def rate: Double
	
	private val invokeUpdate = () => {
		update(Updatable.currentTime)
		_elapsed = 0.0
		Updatable.synchronized {
			Updatable.notifyAll()
		}
	}
	def update(time: Double): Unit
	
	def start() = Updatable.add(this)
}

abstract class UpdatableImpl(val rate: Double) extends Updatable

object Updatable {
	@volatile private var currentTime = 0.0
	private val thread = createThread()
	private var updatables: List[WeakReference[Updatable]] = Nil
	
	def apply(rate: Double = 0.01)(f: => Unit) = {
		val u = new UpdatableImpl(rate) {
			def update(time: Double) = {
				f
			}
		}
		u.start()
		u
	}
	
	private def add(updatable: Updatable) = synchronized {
		updatables = new WeakReference(updatable) :: updatables
		notifyAll()
	}
	
	private def createThread() = {
		val t = new Thread(FunctionRunnable(run))
		t.setDaemon(true)
		t.start()
		t
	}
	
	private def run() = {
		var last = System.currentTimeMillis
		while (true) {
			val current = System.nanoTime
			val time = (current - last) / 1000000000.0
			last = current
			var waitTime = 10.0
			for (ref <- updatables) {
				val u = ref.get
				if (u == null) {
					// Remove de-referenced Updatable
					synchronized {
						updatables = updatables.filterNot(_ == ref)
					}
				} else if (u.elapsed != -1.0) {
					// Check to determine if update should occur
					u._elapsed += time
					val remaining = u.rate - u.elapsed
					if (remaining > 0.0) {		// Time remaining
						waitTime = min(remaining, waitTime)
					} else {					// Need to update
						u._elapsed = -1.0
						Process(u.invokeUpdate, ProcessHandling.Enqueue)
					}
				}
				
				val delay = round(waitTime * 1000.0)
				if (delay > 0) {
					synchronized {
						wait(delay)
					}
				}
			}
		}
	}
}