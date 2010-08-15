package org.sgine.path

import org.sgine.path.resolve.ReflectionResolver

class OPath {

}

object OPath {
	private var resolvers = Map.empty[Class[_], Function2[Any, String, AnyRef]]
	addResolver(classOf[AnyRef], ReflectionResolver)
	
	def apply(root: AnyRef, path: String) = {
		// TODO: support
	}
	
	def resolve(root: AnyRef, path: String) = resolveHierarchically(root, path.split("\\.").toList)
	
	def change(root: AnyRef, path: String, value: AnyRef) = {
		// TODO: support modification of value via path
	}
	
	private def resolveHierarchically(root: AnyRef, path: List[String]): Option[Any] = {
		resolveElement(root, path.head) match {
			case None => None
			case Some(e) => if (path.tail != Nil) {
				resolveHierarchically(e.asInstanceOf[AnyRef], path.tail)
			} else {
				Some(e)
			}
		}
	}
	
	private def resolveElement(root: Any, key: String) = root match {
		case ps: PathSupport => ps.resolveElement(key) match {
			case Some(e) => Some(e)
			case None => useResolver(root.asInstanceOf[AnyRef].getClass, root, key)
		}
		case _ => useResolver(root.asInstanceOf[AnyRef].getClass, root, key)
	}
	
	def addResolver(c: Class[_], f: (Any, String) => AnyRef) = {
		synchronized {
			resolvers += c -> f
		}
	}
	
	@scala.annotation.tailrec
	def useResolver(c: Class[_], root: Any, key: String): Option[Any] = {
		resolvers.get(c) match {
			case Some(r) => r(root, key) match {
				case Some(s) => Some(s)
				case None => c.getSuperclass match {
					case null => None
					case s => useResolver(s, root, key)
				}
			}
			case None => c.getSuperclass match {
				case null => None
				case s => useResolver(s, root, key)
			}
		}
	}
}