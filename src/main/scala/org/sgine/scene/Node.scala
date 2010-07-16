package org.sgine.scene

import org.sgine.event._

/**
 * Represents some object in a scenegraph.
 *
 * Can be something visible, or just pure information organized in a searchable tree.
 */
trait Node extends Listenable {
	private var parentContainer: NodeContainer = _
	
	def parent_=(parent: NodeContainer) = this.parentContainer = parent
	
	/**
	 * The container that this node is located in, or null if it is not located in any collection.
	 */
	override def parent = parentContainer
	
	@scala.annotation.tailrec
	final def next(f: Node => Boolean): Node = {
		val n = nextNode()
		if (n == null) {
			null
		} else if (f(n)) {
			n
		} else {
			n.next(f)
		}
	}
	
	@scala.annotation.tailrec
	final def previous(f: Node => Boolean): Node = {
		val n = previousNode()
		if (n == null) {
			null
		} else if (f(n)) {
			n
		} else {
			n.previous(f)
		}
	}
	
	def nextIterator = new HierarchicalIterator(this, true)
	
	def previousIterator = new HierarchicalIterator(this, false)
	
	def previousNode() = Node.previous(this) match {
		case Some(s) => s
		case None => null
	}
	
	def nextNode() = Node.next(this) match {
		case Some(s) => s
		case None => null
	}
	
	def hierarchyString(): String = {
		if (parent != null) {
			parent.hierarchyString + "(" + parent.indexOf(this) + ") -> " + getClass.getSimpleName
		} else {
			getClass.getSimpleName
		}
	}
	
	def root: Node = parent match {
		case null => this
		case _ => parent.root
	}
	
	def lastNode = lastNodeInternal(root)
	
	private def lastNodeInternal(n: Node): Node = n match {
		case container: NodeContainer => {
			if (container.size > 0) {
				lastNodeInternal(container.last)
			} else {
				container
			}
		}
		case _ => n
	}
	
	override def toString() = getClass.getSimpleName
}

class HierarchicalIterator(var node: Node, forward: Boolean) extends Iterator[Node] {
	private var nextNode: Node = null
	
	def hasNext = {
		if ((nextNode == null) && (node != null)) {
			if (forward) {
				nextNode = node.nextNode()
			} else {
				nextNode = node.previousNode()
			}
		}
		nextNode != null
	}
	
	def next = {
		hasNext
		node = nextNode
		nextNode = null
		node
	}
}

object Node {
	protected def previous(n: Node) = {
		previousSibling(n.parent, n)
	}
	
	private def previousSibling(parent: NodeContainer, child: Node): Option[Node] = {
		if (parent == null) {
			return None
		}
		var last: Node = null
		for (c <- parent) {
			if (child == c) {
				last match {
					case null => return Some(parent)
					case container: NodeContainer => return lastChild(container)
					case _ => return Some(last)
				}
			}
			last = c
		}
		return None
	}
	
	private def lastChild(container: NodeContainer): Option[Node] = {
		if (container.size > 0) {
			var l: Node = null
			for (child <- container) {
				l = child
			}
			l match {
				case cnt: NodeContainer => lastChild(cnt)
				case _ => Some(l)
			}
		} else {
			Some(container)
		}
	}
	
	protected def next(n: Node) = {
		nextChild(n) match {		// Check to see if "n" has children
			case Some(child) => Some(child)
			case None => nextSibling(n.parent, n)
		}
	}
	
	private def nextChild(n: Node): Option[Node] = n match {
		case c: NodeContainer => {
			if (c.size > 0) {
				Some(c.head)
			} else {
				None
			}
		}
		case _ => None
	}
	
	private def nextSibling(parent: NodeContainer, child: Node): Option[Node] = {
		if (parent == null) {
			return None
		}
		var found = false
		for (c <- parent) {
			if (c == child) {
				found = true
			} else if (found) {
				return Some(c)
			}
		}
		return nextSibling(parent.parent, parent)
	}
}