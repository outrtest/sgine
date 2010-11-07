package org.sgine.scene.view

import java.util.concurrent._

import org.sgine.core.ProcessingMode

import org.sgine.event._

import org.sgine.scene._
import org.sgine.scene.event._
import org.sgine.scene.query._
import org.sgine.scene.view.event._

import org.sgine.work.Worker

import scala.collection.mutable.ArrayBuffer

/**
 * A view to some Nodes that match a query in a NodeContainer.
 */
class NodeView private (node: Node, query: Function1[Node, Boolean], sortFunction: (Node, Node) => Int, filterFunction: (Node) => Boolean) extends Iterable[Node] with Listenable {
	private var queue = new ArrayBuffer[Node] {
		def localArray = array
	}
	private var excluded = new ArrayBuffer[Node]
	
	private val comparator = if (sortFunction != null) {
		new java.util.Comparator[AnyRef] {
			def compare(o1: AnyRef, o2: AnyRef) = sortFunction(o1.asInstanceOf[Node], o2.asInstanceOf[Node])
		}
	} else {
		null
	}
	
	private val add = (n: Node) => if (!queue.contains(n)) {
		queue += n
		Event.enqueue(NodeAddedEvent(this, n))
	}
	
	private val remove = (n: Node) => if (queue.contains(n)) {
		queue -= n
		Event.enqueue(NodeRemovedEvent(this, n))
	}
	
	def iterator = queue.iterator
	
	/**
	 * Invokes the initial query to populate information into the NodeView
	 */
	private def refresh() = {
		queue.clear()
		excluded.clear()
		NodeQuery.query(query, node, add)
		filter()
		sort()
	}
	
	def filter() = {
		if (filterFunction != null) {
			// Check to see if any excluded should be brought back into the fold
			if (excluded.length > 0) {
				for (index <- 0 until excluded.length reverse) {		// Go in reverse since we're working on indexes
					val ex = excluded(index)
					if (!filterFunction(ex)) {		// Should not be filtered
						excluded.remove(index)
						queue += ex
					}
				}
			}
			
			// Filter out the unwanted
			if (queue.length > 0) {
				for (index <- 0 until queue.length reverse) {			// Go in reverse since we're working on indexes
					val in = queue(index)
					if (filterFunction(in)) {		// Should be filtered
						queue.remove(index)
						excluded += in
					}
				}
			}
		}
	}
	
	def sort() = if ((comparator != null) && (queue.length > 0)) {
		java.util.Arrays.sort(queue.localArray, 0, queue.length, comparator)
	}
	
	private def containerEvent(evt: NodeContainerEvent) = {
		if (evt.eventType == SceneEventType.ChildAdded) {
			NodeQuery.query(query, evt.child, add)
		} else if (evt.eventType == SceneEventType.ChildRemoved) {
			NodeQuery.query(query, evt.child, remove)
		}
	}
}

object NodeView {
	def apply(node: Node, query: Node => Boolean, processingMode: ProcessingMode, worker: Worker, sortFunction: (Node, Node) => Int = null, filterFunction: (Node) => Boolean = null): NodeView = {
		val v = new NodeView(node, query, sortFunction, filterFunction)
		v.refresh()		// Refresh the view contents
		// Listen for events
		node.listeners += EventHandler(v.containerEvent, processingMode, Recursion.Children, worker = worker)
		v
	}
}