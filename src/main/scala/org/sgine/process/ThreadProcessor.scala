package org.sgine.process

import java.util.concurrent.atomic.AtomicReference

import org.sgine.util.FunctionRunnable

/**
 * Processor instance that wraps around a single
 * java.lang.Thread.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ThreadProcessor extends Processor {
	private val thread = createThread()
	private val ref = new AtomicReference[() => Any]
	private var keepAlive = true
	
	def accept(f: () => Any) = if (keepAlive && ref.compareAndSet(null, f)) {
		synchronized {
			notifyAll()
			true
		}
	} else {
		false
	}
	
	/**
	 * The state of this Processor.
	 * 
	 * @return
	 * 		true if alive
	 */
	def alive = keepAlive || ref.get != null
	
	/**
	 * Shuts this Processor down if not already.
	 */
	def shutdown() = keepAlive = false
	
	private def createThread() = {
		val t = new Thread(FunctionRunnable(run))
		t.setDaemon(true)
		t.start()
		t
	}
	
	private def run() = {
		while (keepAlive) {
			var f: () => Any = null
			synchronized {
				f = ref.getAndSet(null)
				if (f == null) {
					wait()
				}
			}
			if (f != null) {
				f()
			}
		}
	}
}