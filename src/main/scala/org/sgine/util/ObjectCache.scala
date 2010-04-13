package org.sgine.util

import java.util.concurrent.ArrayBlockingQueue

abstract class ObjectCache[T] (val max: Int = 1000) {
	private val cache = new ArrayBlockingQueue[T](max)
	
	def request() = {
		val e = cache.poll()
		if (e != null) {
			e
		} else {
			create()
		}
	}
	
	def create(): T
	
	def release(t: T) = cache.add(t)
}