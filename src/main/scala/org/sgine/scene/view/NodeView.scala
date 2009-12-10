package org.sgine.scene.view

import java.util.concurrent._

import org.sgine.event._

import org.sgine.scene._
import org.sgine.scene.event._
import org.sgine.scene.query._
import org.sgine.scene.view.event._

import scala.collection.JavaConversions._

/**
 * A view to some Nodes that match a query in a NodeContainer.
 */
class NodeView private (container: NodeContainer, query: NodeQuery) extends Iterable[Node] with Listenable {
	val parent = null
	private val queue = new ConcurrentLinkedQueue[Node]
	
	def iterator = queue.iterator
	
	/**
	 * Invokes the initial query to populate information into the NodeView
	 */
	private def refresh() = {
		NodeQuery.query(query, container, add)
	}
	
	private def containerEvent(evt: NodeContainerEvent) = {
		if (evt.eventType == SceneEventType.ChildAdded) {
			NodeQuery.query(query, evt.child, add)
		} else if (evt.eventType == SceneEventType.ChildRemoved) {
			NodeQuery.query(query, evt.child, remove)
		}
	}
	
	private def add(n: Node) = {
		if (queue.add(n)) {
			Event.enqueue(NodeAddedEvent(this, n))
		}
	}
	
	private def remove(n: Node) = {
		if (queue.remove(n)) {
			Event.enqueue(NodeRemovedEvent(this, n))
		}
	}
}

object NodeView {
	def apply(container: NodeContainer, query: NodeQuery, asynchronous: Boolean): NodeView = {
		val v = new NodeView(container, query)
		v.refresh()
		val h = container.listeners += v.containerEvent _
		h.recursion = Recursion.Children
		if (!asynchronous) {
			h.processingMode = ProcessingMode.Blocking
		}
		v
	}
}