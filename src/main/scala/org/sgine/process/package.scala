package org.sgine

package object process {
	def update(rate: Double = 0.01)(f: => Unit) = Updatable(rate)(f)
	
	def attempt(f: => Unit): Boolean = Process(() => f, ProcessHandling.Attempt)
	
	def asynchronous(f: => Unit): Boolean = Process(() => f, ProcessHandling.Enqueue)
	
	def start(f: => Unit): Boolean = Process(() => f, ProcessHandling.Wait)
}