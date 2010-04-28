package org.sgine.core

import java.net.URL

class Resource private(val url: URL)

object Resource {
	private var paths: List[ResourcePath] = Nil
	addPath("resource/")
	addPath("resource/font/")
	
	def addPath(path: String, finder: ResourceFinder = ClassLoaderResourceFinder) = {
		var p = path
		if ((!p.endsWith("/")) && (!p.endsWith("\\"))) {		// Make sure path ends with slash
			p = p + "/"
		}
		paths = ResourcePath(path, finder) :: paths
	}
	
	def apply(url: URL) = new Resource(url)
	
	def apply(name: String): Resource = {
		for (p <- paths) {
			val url = p.find(name)
			if (url != null) {
				return new Resource(url)
			}
		}
		throw new RuntimeException("Resource lookup failed: " + name)
	}
	
//	def apply(path: String) = new Resource(getClass.getClassLoader.getResource(path))
	
	def apply(file: java.io.File) = new Resource(file.toURI.toURL)
}

case class ResourcePath(path: String, finder: ResourceFinder) {
	def find(name: String) = finder.find(path, name)
}

trait ResourceFinder {
	def find(path: String, name: String): URL
}

object ClassLoaderResourceFinder extends ResourceFinder {
	def find(path: String, name: String) = getClass.getClassLoader.getResource(path + name)
}