package org.sgine.core

trait Modifiable {
	private var _lastModified = 0L
	
	def lastModified = _lastModified
	
	def modified() = _lastModified = System.nanoTime
}