package org.sgine.bus

import org.sgine.work.WorkManager

import scala.reflect.Manifest

class ObjectBus protected(val name: String, val workManager: WorkManager) {
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
		if (workManager != null) {
			workManager += (() => processMessage(message, nodes))
		} else {
			processMessage(message, nodes)
		}
	}
	
	@scala.annotation.tailrec
	private def processMessage[T](message: T, nodes: List[ObjectNode[_]])(implicit manifest: Manifest[T]): Unit = {
		if (nodes != Nil) {
			val node = nodes.head
			if (node(message) != Routing.Stop) {
				processMessage(message, nodes.tail)
			}
		}
	}
	
	private val prioritySort = (a: ObjectNode[_], b: ObjectNode[_]) => a.priority > b.priority
}

object ObjectBus {
	private var map = Map.empty[String, ObjectBus]
	
	def apply(name: String, workManager: WorkManager = null) = {
		synchronized {
			map.get(name) match {
				case Some(ob) => ob
				case None => {
					val ob = new ObjectBus(name, workManager)
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
	def apply() = new ObjectBus(null, null)
}