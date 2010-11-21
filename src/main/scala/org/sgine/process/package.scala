package org.sgine

import java.util.Calendar

import org.sgine.process.updatable._

package object process {
	def update(rate: Double = 0.01, count: Int = -1)(f: => Unit) = {
		val r = rate
		val c = count
		new FunctionUpdatable(() => f) with TimedUpdatable with CountedUpdatable {
			def rate = r
			def count = c
		}.start()
	}
	
	def invokeLater(distance: Double)(f: => Unit) = update(distance, 1)(f)
	
	def invokeScheduled(calendar: Calendar)(f: => Unit): Unit = {
		val now = Calendar.getInstance
		val distance = (now.getTimeInMillis - calendar.getTimeInMillis) / 1000.0
		invokeLater(distance)(f)
	}
	
	def attempt(f: => Unit): Boolean = Process(() => f, ProcessHandling.Attempt)
	
	def asynchronous(f: => Unit): Boolean = Process(() => f, ProcessHandling.Enqueue)
	
	def start(f: => Unit): Boolean = Process(() => f, ProcessHandling.Wait)
}