package org.sgine.process

import java.lang.ref.WeakReference

import org.sgine.util.FunctionRunnable

import scala.math._

trait Updatable {
	@volatile private var _elapsed = 0.0
	
	def elapsed = _elapsed
	def rate: Double
	def count: Int
	
	private var keepAlive = true
	@volatile private var updated = 0
	
	private val invokeUpdate = () => {
		update(Updatable.currentTime)
		updated += 1
		_elapsed = 0.0
		if (count > 0 && updated >= count) {		// Updated the number of times desired
			shutdown()
		}
		Updatable.synchronized {
			Updatable.changed.set(true)
			Updatable.notifyAll()
		}
	}
	def update(time: Double): Unit
	
	def start() = Updatable.add(this)
	
	protected def shutdown() = keepAlive = false
}

abstract class UpdatableImpl(val rate: Double, val count: Int) extends Updatable

object Updatable {
	@volatile private var currentTime = 0.0
	private val thread = createThread()
	private var updatables: List[WeakReference[Updatable]] = Nil
	private val changed = new java.util.concurrent.atomic.AtomicBoolean(false)
	
	def apply(rate: Double = 0.01, count: Int = -1)(f: => Unit) = {
		val u = new UpdatableImpl(rate, count) {
			def update(time: Double) = {
				f
			}
		}
		u.start()
		u
	}
	
	private def add(updatable: Updatable) = synchronized {
		updatables = new WeakReference(updatable) :: updatables
		changed.set(true)
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
				if ((u == null) || (!u.keepAlive)) {
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
			}
			
			val delay = round(waitTime * 1000.0)
			if (delay > 0) {
				synchronized {
					if (!changed.compareAndSet(true, false)) {
						wait(delay)
					}
				}
			}
		}
	}
}