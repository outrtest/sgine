package org.sgine.path

trait PathSupport {
	final def resolve(path: String) = OPath.resolve(this, path)
	
	final def resolveStructure(path: String) = OPath.resolveStructure(this, path)
	
	def resolveElement(key: String): Option[Any]
}