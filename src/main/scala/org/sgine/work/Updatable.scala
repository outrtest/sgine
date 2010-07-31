package org.sgine.work

import java.lang.ref.WeakReference

import scala.collection.mutable.ArrayBuffer

import scala.math._

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
	
	private var array = new ArrayBuffer[WeakReference[Updatable]]()
	
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
				Thread.sleep(shouldWait)
			}
		}
	}
	
	var lastTime: Long = System.nanoTime
	
	def update() = {
		val time = System.nanoTime
		val frequency = (time - lastTime) / 1000000000.0
		lastTime = time
		val shouldElapse = round(1000.0 / Rate)
		for (wr <- array) {
			val u = wr.get
			if (u == null) {
				synchronized {
					array -= wr
				}
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
			array += new WeakReference(u)
		}
	}
	
	private def remove(u: Updatable) = {
		synchronized {
			array.find(wr => wr.get == u) match {
				case Some(r) => array -= r
				case None =>
			}
		}
	}
}