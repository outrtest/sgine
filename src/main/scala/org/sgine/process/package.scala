package org.sgine

import java.util.Calendar

import org.sgine.process.updatable._

import org.sgine.util.FutureFunction

package object process {
	def update(rate: Double = 0.01, count: Int = -1)(f: => Unit) = {
		val r = rate
		val c = count
		val u = new FunctionUpdatable(() => f) with TimedUpdatable with CountedUpdatable {
			override protected val refType = org.sgine.util.ReferenceType.Hard
			
			def rate = r
			def count = c
		}
		u.start()
		
		u
	}
	
	def invokeLater(distance: Double)(f: => Unit) = update(distance, 1)(f)
	
	def invokeScheduled(calendar: Calendar)(f: => Unit): Unit = {
		val now = Calendar.getInstance
		val distance = (now.getTimeInMillis - calendar.getTimeInMillis) / 1000.0
		invokeLater(distance)(f)
	}
	
	def attempt(f: => Unit): Boolean = Process(() => f, ProcessHandling.Attempt)
	
	def asynchronous(f: => Unit): Boolean = Process(() => f, ProcessHandling.Enqueue)
	
	def future[T](f: => T) = {
		val func = FutureFunction(f)
		Process(func, ProcessHandling.Enqueue)
		func
	}
	
	def start(f: => Unit): Boolean = Process(() => f, ProcessHandling.Wait)
	
	def executor[T](f: (T) => Unit) = {
		new PublicExecutor[T] {
			protected def execute(value: T) = f(value)
		}
	}
}