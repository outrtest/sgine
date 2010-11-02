package org.sgine.scene.view

import java.util.concurrent._

import org.sgine.core.ProcessingMode

import org.sgine.event._

import org.sgine.scene._
import org.sgine.scene.event._
import org.sgine.scene.query._
import org.sgine.scene.view.event._

import scala.collection.mutable.ArrayBuffer

/**
 * A view to some Nodes that match a query in a NodeContainer.
 */
class NodeView private (node: Node, query: Function1[Node, Boolean]) extends Iterable[Node] with Listenable {
	private var queue = new ArrayBuffer[Node] {
		def localArray = array
	}
	
	private var comparator: java.util.Comparator[AnyRef] = _
	
	def sortFunction_=(f: (Node, Node) => Int) = {
		comparator = new java.util.Comparator[AnyRef] {
			def compare(o1: AnyRef, o2: AnyRef) = f(o1.asInstanceOf[Node], o2.asInstanceOf[Node])
		}
		_sortFunction = f
	}
	
	def sortFunction = _sortFunction
	
	private var _sortFunction: (Node, Node) => Int = _
	
	def iterator = queue.iterator
	
	/**
	 * Invokes the initial query to populate information into the NodeView
	 */
	private def refresh() = {
		queue.clear()
		NodeQuery.query(query, node, add)
		sort()
		// TODO: add frustum culling via filter method?
	}
	
	def sort() = {
//		if (sortFunction != null) {
//			org.sgine.util.Sort.bubbleSort(queue, sortFunction)
//		}
		if (queue.length > 0) {
			java.util.Arrays.sort(queue.localArray, 0, queue.length, comparator)
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
	def apply(node: Node, query: Node => Boolean, asynchronous: Boolean): NodeView = {
		val v = new NodeView(node, query)
		v.refresh()
		val h = node.listeners += v.containerEvent _
		h.recursion = Recursion.Children
		if (!asynchronous) {
			h.processingMode = ProcessingMode.Blocking
		}
		v
	}
}