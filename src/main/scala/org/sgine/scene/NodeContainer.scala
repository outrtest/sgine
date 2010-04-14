package org.sgine.scene


import org.sgine.scene.query._
import org.sgine.scene.view.NodeView

/**
 * Something that contains Nodes and can fetch the ones mathcing a query.
 */
trait NodeContainer extends Node with Iterable[Node]