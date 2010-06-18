package org.sgine.space

import bounds.Bounds
import org.sgine.math.Vector3
import org.sgine.scene.query.NodeQuery
import org.sgine.scene.Node

/**
 * Represents some fixed part of a Space.
 */
trait Region extends NodeQuery {

  /**
   * True if the node is a region that is in this Region, false othwerwise.
   */
  def matches(node: Node): Boolean = node match {
    case (x: LocatedNode) => intersects(x)
    case _ => false
  }

  def position:Vector3

  def bounds:Bounds

  def intersects(region: Region ):Boolean = bounds.intersects( position, region.position, region.bounds )
  def intersects(pos: Vector3, otherBounds: Bounds) : Boolean = bounds.intersects( position, pos, otherBounds )

}
