package org.sgine.scene

/**
 * Represents some object in a scenegraph.
 *
 * Can be something visible, or just pure information organized in a searchable tree.
 */
trait Node {

  /**
   * The container that this node is located in, or null if it is not located in any collection.
   */
  def parent: NodeContainer

  /**
   * Changes the parent NodeContainer of this Node.
   */
  def setParent(container: NodeContainer)
}
