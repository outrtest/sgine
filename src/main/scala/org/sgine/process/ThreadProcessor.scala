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
	private val ref = new AtomicReference[() => Unit]
	private var keepAlive = true
	
	def accept(f: () => Unit) = if (keepAlive && ref.compareAndSet(null, f)) {
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
			val f = synchronized {
				ref.get match {
					case null => {
						wait()
						null
					}
					case f => f
				}
			}
			if (f != null) {
				// Invoke function
				f()
				// Poll from the queue
				ref.set(Process.poll())
			}
		}
	}
}