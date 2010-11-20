package org.sgine

import java.util.Calendar

package object process {
	def update(rate: Double = 0.01, count: Int = -1)(f: => Unit) = Updatable(rate, count)(f)
	
	def invokeLater(distance: Double)(f: => Unit) = new Updatable {
		val rate = distance
		val count = -1
		def update(time: Double) = {
			f
			shutdown()
		}
	}
	
	def invokeLater(calendar: Calendar)(f: => Unit): Unit = {
		val now = Calendar.getInstance
		val distance = (now.getTimeInMillis - calendar.getTimeInMillis) / 1000.0
		invokeLater(distance)(f)
	}
	
	def attempt(f: => Unit): Boolean = Process(() => f, ProcessHandling.Attempt)
	
	def asynchronous(f: => Unit): Boolean = Process(() => f, ProcessHandling.Enqueue)
	
	def start(f: => Unit): Boolean = Process(() => f, ProcessHandling.Wait)
}