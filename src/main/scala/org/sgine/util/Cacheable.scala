package org.sgine.util

trait Cacheable[T] {
	def cache: ObjectCache[T]
}