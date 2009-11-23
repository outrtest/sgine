package com.sgine.space

import com.sgine.math.Vector3

/**
 * Some approximation for a shape.  Used e.g. when querying Space:s for Node:s.
 *
 * Doesn't contain location information, just shape information.
 */
trait Bounds {

  /**
   * True if the other bounds overlaps or is inside these Bounds, with the specified locations.
   */
  def intersects( location : Vector3, otherLocation : Vector3, otherBounds : Bounds ) : Boolean

}
