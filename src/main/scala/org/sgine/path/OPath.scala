package org.sgine.path

import org.sgine.path.resolve.ReflectionResolver

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.Listenable
import org.sgine.event.ProcessingMode

import scala.reflect.Manifest

/**
 * OPath represents an Object path from <code>root</code>. An Object path is
 * a String-defined path to resolve a specific object. If this path is specified
 * as <code>dynamic</code>, PathChangeEvents will be thrown when an element or
 * end-point on a path changes.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class OPath[T] private(val root: AnyRef, val path: List[String], val dynamic: Boolean)(implicit manifest: Manifest[T]) extends Listenable {
	lazy val elements = new Array[OPathElement](path.length)
	private val handler = EventHandler(updateElements, ProcessingMode.Blocking)
	private var defaultValue: T = _
	private var current: T = _
	
	updateElements()
	
	private def updateElements(evt: Event = null): Unit = {
		if ((evt == null) || (evt.isInstanceOf[PathElementChangeEvent])) {
			val structure = OPath.resolveHierarchically(root, path, Nil)
			
			var changed = false
			for (index <- 0 until path.length) {
				val e = elements(index) match {
					case null => null
					case element => element.value.getOrElse(null)
				}
				val s = if (structure.length > index) {
					structure(index).getOrElse(null)
				} else {
					null
				}
				if (e != s) {							// Different, need to change
					if (dynamic) {
						e match {						// Disconnect changed
							case l: Listenable => l.listeners -= handler
							case _ =>
						}
					}
					
					// Assign new path element
					elements(index) = OPathElement(path(index), if (s != null) Some(s) else None)
					
					changed = true
					
					if (dynamic) {
						s match {						// Connect new element
							case l: Listenable => l.listeners += handler
							case _ =>
						}
					}
				}
			}
			
			if (changed) {
				val previous = current
				current = apply() match {
					case Some(c) => c
					case None => defaultValue
				}
				
				val evt = new PathChangeEvent[T](this, previous, current)
				Event.enqueue(evt)
			}
		}
	}
	
	def apply() = elements(0) match {
		case null => None
		case e => e.value match {
			case None => None
			case v => v.asInstanceOf[Some[T]]
		}
	}
}

object OPath {
	private var resolvers = Map.empty[Class[_], Function2[Any, String, AnyRef]]
	addResolver(classOf[AnyRef], ReflectionResolver)
	
	def apply[T](root: AnyRef, path: String, dynamic: Boolean = true)(implicit manifest: Manifest[T]) = new OPath[T](root, splitPath(path), dynamic)
	
	def resolve(root: AnyRef, path: String) = resolveStructure(root, path).head
	
	def resolveStructure(root: AnyRef, path: String) = resolveHierarchically(root, splitPath(path), Nil)
	
	def change(root: AnyRef, path: String, value: AnyRef) = {
		// TODO: support modification of value via path
	}
	
	@scala.annotation.tailrec
	protected def resolveHierarchically(root: AnyRef, path: List[String], results: List[Option[Any]]): List[Option[Any]] = {
		resolveElement(root, path.head) match {
			case None => None :: results
			case Some(e) => if (path.tail != Nil) {
				resolveHierarchically(e.asInstanceOf[AnyRef], path.tail, Some(e) :: results)
			} else {
				Some(e) :: results
			}
		}
	}
	
	private def resolveElement(root: Any, key: String) = root match {
		case null => None
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
	
	def splitPath(path: String) = {
		var list: List[String] = Nil
		val parser = path.split("\\.")
		for (s <- parser) {
			if (s.indexOf('(') != -1) {
				list = s.substring(s.indexOf('(')) :: s.substring(0, s.indexOf('(')) :: list
			} else if (s.indexOf('[') != -1) {
				list = s.substring(s.indexOf('[')) :: s.substring(0, s.indexOf('[')) :: list
			} else {
				list = s :: list
			}
		}
		list.reverse
	}
}