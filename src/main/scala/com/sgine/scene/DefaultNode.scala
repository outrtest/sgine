package com.sgine.scene

import com.sgine.scene.{NodeContainer, Node}

/**
 * A default implementation of Node.
 */
case class DefaultNode( var parent: NodeContainer ) extends Node {

  def setParent(container: NodeContainer) = parent = container
}
