package com.sgine.scene.space

import bounds.Bounds
import com.sgine.math.Vector3

/**
 * Represents some fixed part of a Space.
 */
trait Region {

  def position:Vector3

  def bounds:Bounds

  def intersects(region: Region ):Boolean = bounds.intersects( position, region.position, region.bounds )
  def intersects(pos: Vector3, otherBounds: Bounds) : Boolean = bounds.intersects( position, pos, otherBounds )

}
