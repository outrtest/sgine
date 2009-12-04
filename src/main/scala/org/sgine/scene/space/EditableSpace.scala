package org.sgine.scene.space

import org.sgine.scene._

/**
 * A Space where Nodes can be manually added and removed.
 */
trait EditableSpace extends Space {

  /**
   * Adds a Node to this Space.
   */
  def addNode( node : Node )

  /**
   * Removes the Node from this Space.
   */
  def removeNode( node : Node )

}
