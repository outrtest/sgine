package org.sgine.scene

import org.sgine.event._

/**
 * Represents some object in a scenegraph.
 *
 * Can be something visible, or just pure information organized in a searchable tree.
 */
trait Node extends Listenable {
	private var parentNode: NodeContainer = _
	
	protected[scene] def parent_=(parentNode: NodeContainer) = this.parentNode = parentNode
	
	/**
	 * The container that this node is located in, or null if it is not located in any collection.
	 */
	def parent = parentNode
	
	val listeners = new EventProcessor(this)
}
