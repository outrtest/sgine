package org.sgine.util

import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.TimeoutException
import java.util.concurrent.TimeUnit

class FutureFunction[T](f: () => T) extends Future[T] with Function0[Unit] {
	@volatile private var response: Option[T] = null
	@volatile private var cancelled = false
	@volatile private var thread: Thread = null
	@volatile private var exception: Throwable = null
	
	/**
	 * Invoked internally by the thread processing the future.
	 */
	def apply() = {
		thread = Thread.currentThread
		try {
			if (!cancelled) {
				response = Some(f())
			}
		} catch {
			case t => exception = t; response = None
		} finally {
			thread = null
		}
	}
	
	def isDone() = response != null
	
	def cancel(mayInterruptIfRunning: Boolean) = {
		val t = thread
		if (t != null) {
			if (mayInterruptIfRunning) {
				cancelled = true
				t.interrupt()
				response = None
				true
			} else {
				false
			}
		} else if (response == null) {		// Hasn't run yet
			cancelled = true
			response = None
			true
		} else {
			false
		}
	}
	
	def isCancelled = cancelled
	
	def get() = get(0.0)
	
	def get(timeout: Long, unit: TimeUnit) = get(unit.toMillis(timeout) / 1000.0)
	
	def get(time: Double) = {
		Time.waitFor(time) {
			response != null
		}
		if (isCancelled) throw new CancellationException()
		else if (exception != null) throw new ExecutionException(exception)
		else if (response == null) throw new TimeoutException()
		
		response.get
	}
}

object FutureFunction {
	def apply[T](f: => T) = new FutureFunction(() => f)
}