package org.sgine.scene.view

import java.util.concurrent._

import org.sgine.event._

import org.sgine.scene._
import org.sgine.scene.event._
import org.sgine.scene.query._
import org.sgine.scene.view.event._

import scala.collection.mutable.ArrayBuffer

/**
 * A view to some Nodes that match a query in a NodeContainer.
 */
class NodeView private (container: NodeContainer, query: Function1[Node, Boolean]) extends Iterable[Node] with Listenable {
	private var queue = new ArrayBuffer[Node]
	
	var sortFunction: (Node, Node) => Boolean = _
	
	def iterator = queue.iterator
	
	/**
	 * Invokes the initial query to populate information into the NodeView
	 */
	private def refresh() = {
		NodeQuery.query(query, container, add)
		sort()
	}
	
	def sort() = {
		if (sortFunction != null) {
			// TODO: how do we avoid recreating?
//			queue = queue.sortWith(sortFunction)
			org.sgine.util.Sort.bubbleSort(queue, sortFunction)
		}
	}
	
	private def containerEvent(evt: NodeContainerEvent) = {
		if (evt.eventType == SceneEventType.ChildAdded) {
			NodeQuery.query(query, evt.child, add)
		} else if (evt.eventType == SceneEventType.ChildRemoved) {
			NodeQuery.query(query, evt.child, remove)
		}
	}
	
	private def add(n: Node) = {
		synchronized {
			if (!queue.contains(n)) {
//				queue = n :: queue
				queue += n
				
				Event.enqueue(NodeAddedEvent(this, n))
			}
		}
	}
	
	private def remove(n: Node) = {
		synchronized {
			if (queue.contains(n)) {
//				queue = queue.filterNot(_ == n)
				queue -= n
				
				Event.enqueue(NodeRemovedEvent(this, n))
			}
		}
	}
}

object NodeView {
	def apply(container: NodeContainer, query: Node => Boolean, asynchronous: Boolean): NodeView = {
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