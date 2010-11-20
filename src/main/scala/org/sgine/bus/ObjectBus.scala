package org.sgine.bus

import org.sgine.work.WorkManager

import scala.reflect.Manifest

class ObjectBus protected(val name: String) {
	private var nodes: List[ObjectNode[_]] = Nil
	
	def +=(node: ObjectNode[_]) = {
		if (node == null) throw new NullPointerException("Null ObjectNodes not allowed.")
		synchronized {
			nodes = node :: nodes
			nodes = nodes.sortWith(prioritySort)
		}
	}
	
	def -=(node: ObjectNode[_]) = {
		synchronized {
			nodes = nodes.filterNot(n => n == node)
		}
	}
	
	def process[T](message: T)(implicit manifest: Manifest[T]) = {
		processMessage(message, nodes)
	}
	
	@scala.annotation.tailrec
	private def processMessage[T](message: T, nodes: List[ObjectNode[_]])(implicit manifest: Manifest[T]): Boolean = {
		if (nodes != Nil) {
			val node = nodes.head
			if (node.receive(message) != Routing.Stop) {
				processMessage(message, nodes.tail)
			} else {
				true
			}
		} else {
			false
		}
	}
	
	private val prioritySort = (a: ObjectNode[_], b: ObjectNode[_]) => a.priority > b.priority
}

object ObjectBus {
	private var map = Map.empty[String, ObjectBus]
	
	def apply(name: String) = {
		synchronized {
			map.get(name) match {
				case Some(ob) => ob
				case None => {
					val ob = new ObjectBus(name)
					map += name -> ob
					ob
				}
			}
		}
	}
	
	/**
	 * Creates an anonymous ObjectBus that cannot be referenced
	 * by name.
	 * 
	 * @return
	 * 		new ObjectBus
	 */
	def apply() = new ObjectBus(null)
}