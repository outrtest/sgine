package org.sgine.core

import java.net.URL

class Resource private(val url: URL)

object Resource {
	def apply(url: URL) = new Resource(url)
	
	def apply(path: String) = new Resource(getClass.getClassLoader.getResource(path))
	
	def apply(file: java.io.File) = new Resource(file.toURI.toURL)
}