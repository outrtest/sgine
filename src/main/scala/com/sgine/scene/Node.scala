package com.sgine.scene

/**
 * Represents some object in a scenegraph.
 *
 * Can be something visible, or just pure information organized in a searchable tree.
 */
trait Node {

  def parent: Node

}
