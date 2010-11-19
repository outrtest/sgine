package org.sgine.process

import org.sgine.bus.ObjectBus

import scala.math._

/**
 * Process is an threading helper class to provide asynchronous
 * and synchronous operations efficiently along with Thread Pooling.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Process {
	val cpuCount = Runtime.getRuntime.availableProcessors
	val idealThreadCount = max(cpuCount * 2, 4)
	
	private val bus = ObjectBus()
	private var processors: List[Processor] = Nil
	
	initProcessors()
	
	def asynchronous(f: => Any) = apply(() => f)
	
	def apply(f: () => Any) = bus.process(f)
	
	// Initializes ThreadProcessors to double the number
	// of cpuCount.
	private def initProcessors() = {
		for (i <- 0 until idealThreadCount) addProcessor(new ThreadProcessor())
	}
	
	def addProcessor(processor: Processor) = synchronized {
		processors = processor :: processors
		bus += processor
	}
}