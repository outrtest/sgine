package org.sgine.path.resolve

trait Resolver extends Function2[Any, String, Option[AnyRef]] {
	def apply(root: Any, key: String) = resolve(root, key)
	
	def resolve(root: Any, key: String): Option[AnyRef]
}