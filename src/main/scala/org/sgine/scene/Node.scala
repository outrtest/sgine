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
}

object Node {
//	@scala.annotation.tailrec
	final def next(n: Node) = {
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
//			println("Parent is null! - " + child.hierarchyString)
			return None
		} else {
//			println("NEXT SIBLING: " + child.hierarchyString + " - Children: " + parent.size)
		}
		var found = false
		for (c <- parent) {
			if (c == child) {
//				println("\tFOUND!")
				found = true
			} else if (found) {
//				println("\tRETURNING!")
				return Some(c)
			} else {
//				println("\tNO MATCH: " + c.hierarchyString)
			}
		}
		return nextSibling(parent.parent, parent)
	}
}