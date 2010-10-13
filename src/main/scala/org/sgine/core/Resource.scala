package org.sgine.core

import java.net.URL

class Resource private(val url: URL) {
	def parent = new Resource(new URL(url.toString.substring(0, url.toString.lastIndexOf("/"))))
	
	def child(name: String) = new Resource(new URL(url.toString + "/" + name))
	
	override def toString() = "Resource(" + url + ")"
}

object Resource {
	private var paths: List[ResourcePath] = Nil
	addPath("")
	addPath("font/")
	addPath("", FileResourceFinder)
	
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
			p.find(name) match {
				case Some(u) => return new Resource(u)
				case None =>
			}
		}
		throw new RuntimeException("Resource lookup failed: " + name)
	}
	
	def apply(file: java.io.File) = new Resource(file.toURI.toURL)
}

case class ResourcePath(path: String, finder: ResourceFinder) {
	def find(name: String) = finder.find(path, name)
}

trait ResourceFinder {
	def find(path: String, name: String): Option[URL]
}

object ClassLoaderResourceFinder extends ResourceFinder {
	def find(path: String, name: String) = getClass.getClassLoader.getResource(path + name) match {
		case null => None
		case u: URL => Some(u)
	}
}

object FileResourceFinder extends ResourceFinder {
	def find(path: String, name: String) = {
		val f = new java.io.File(path, name)
		if (f.exists) {
			Some(f.toURI.toURL)
		} else {
			None
		}
	}
}