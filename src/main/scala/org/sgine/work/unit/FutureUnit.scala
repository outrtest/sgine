package org.sgine.work.unit

import java.util.concurrent.Future

trait FutureUnit[T] extends WorkUnit with FinishedUnit with Future[T] {
	var value: T = _
	
	private var cancelled: Boolean = _
	private var interrupted: Boolean = _
	private var _finished: Boolean = _
	
	def cancel(mayInterruptIfRunning: Boolean): Boolean = {
		if ((workThread != null) && (!mayInterruptIfRunning)) {
			return false
		}
		cancelled = true
		if (workThread != null) {
			interrupted = true
			workThread.interrupt()
		}
		
		cancelled
	}
	
	def get(): T = get(365, java.util.concurrent.TimeUnit.DAYS)
	
	def get(timeout: Long, unit: java.util.concurrent.TimeUnit): T = {
		val timeoutTime = System.currentTimeMillis + java.util.concurrent.TimeUnit.MILLISECONDS.convert(timeout, unit)
		do {
			if (cancelled) {
				throw new java.util.concurrent.CancellationException()
			}
			
			Thread.sleep(1)
		} while ((!isDone) && (System.currentTimeMillis < timeoutTime))
		if (!isDone) {
			throw new java.util.concurrent.TimeoutException()
		}
		value
	}
	
	def isDone() = _finished
	
	def isInterrupted() = interrupted
	
	def finished() = {
		_finished = true
	}
}