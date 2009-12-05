package org.sgine.scene

import org.sgine.event._

/**
 * Represents some object in a scenegraph.
 *
 * Can be something visible, or just pure information organized in a searchable tree.
 */
trait Node extends Listenable {
	private var parentContainer: NodeContainer = _
	
	protected[scene] def parent_=(parent: NodeContainer) = this.parentContainer = parent
	
	/**
	 * The container that this node is located in, or null if it is not located in any collection.
	 */
	def parent = parentContainer
	
	val listeners = new EventProcessor(this)
}
