package org.sgine.scene

/**
 * A trait for a node container where Nodes can be added or removed.
 */
trait MutableNodeContainer extends NodeContainer {

	/**
	 * Adds the specified node the NodeContainer.
	 * If the node is null or already added an exception is thrown.
	 * Sets the nodes parent to refer to this container.
	 */
	def +=(node: Node)
	
	/**
	 * Removes a specified node from the NodeContainer if it was in it.
	 * Returns true if the node was removed, false if it was not found.
	 * Also sets the nodes parent to null if it was removed.
	 */
	def -=(node: Node): Boolean
}