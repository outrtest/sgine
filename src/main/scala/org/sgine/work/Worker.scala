package org.sgine.work

import org.sgine.log._

import org.sgine.util.Time

trait Worker {
	private val invokeQueue = new java.util.concurrent.ArrayBlockingQueue[Function0[Unit]](500)
	
	def invokeLaterCurry(f: => Unit): Unit = invokeLater(() => f)
	
	def invokeLater(f: () => Unit): Unit = invokeQueue.add(f)
	
	def invokeAndWaitCurry(f: => Unit): Unit = invokeAndWait(() => f)
	
	def invokeAndWait(f: () => Unit): Unit = {
		var finished = false
		val delegate = () => {
			f()
			finished = true
		}
		invokeLater(delegate)
		
		// Wait
		Time.waitFor(-1.0) {
			finished
		}
	}
	
	protected def doWork() = {
		try {
			invokeQueue.poll() match {
				case null =>
				case f => f()
			}
		} catch {
			case exc => trace("Exception thrown while procesing invokeQueue entry.", exc)
		}
	}
}