package org.sgine.process

import org.sgine.bus.ObjectBus
import org.sgine.bus.ObjectNode
import org.sgine.bus.Routing

import scala.math._

import scala.reflect.Manifest

/**
 * Process is an threading helper class to provide asynchronous
 * and synchronous operations efficiently along with Thread Pooling.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Process {
	private val DefaultHandling = ProcessHandling.Enqueue
	
	val cpuCount = Runtime.getRuntime.availableProcessors
	val idealThreadCount = max(cpuCount * 2, 4)
	
	private val bus = ObjectBus()
	private var processors: List[Processor] = Nil
	
	initProcessors()
	
	def attempt(f: => Any): Boolean = apply(() => f, ProcessHandling.Attempt)
	
	def asynchronous(f: => Any): Boolean = apply(() => f, ProcessHandling.Enqueue)
	
	def start(f: => Any): Boolean = apply(() => f, ProcessHandling.Wait)
	
	def apply(f: () => Any,
			  handling: ProcessHandling = DefaultHandling): Boolean = if (bus.process(f)) {
	 	true
	} else if (handling == ProcessHandling.Wait) {
		Thread.sleep(10)
		apply(f, handling)
	} else if (handling == ProcessHandling.Enqueue) {
		// TODO: enqueue
		true
	} else {
		false
	}
	
	private def initProcessors() = {
		// Initializes the processors
		for (i <- 0 until idealThreadCount) addProcessor(new ThreadProcessor())
	}
	
	def addProcessor(processor: Processor) = synchronized {
		processors = processor :: processors
		bus += processor
	}
}