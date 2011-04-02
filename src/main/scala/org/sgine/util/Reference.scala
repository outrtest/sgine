package org.sgine.util

trait Reference[T] {
	def apply(): T
}

object Reference {
	def weak[T](value: T) = new WeakReference(new java.lang.ref.WeakReference(value))
	
	def hard[T](value: T) = new HardReference(value)
}

protected class WeakReference[T](ref: java.lang.ref.WeakReference[T]) extends Reference[T] {
	def apply() = ref.get
}

protected class HardReference[T](value: T) extends Reference[T] {
	def apply() = value
}