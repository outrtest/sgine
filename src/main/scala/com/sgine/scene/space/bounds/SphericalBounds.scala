package com.sgine.scene.space.bounds

import com.sgine.math.Vector3
import com.sgine.scene._

/**
 * Represents a spherical bounding volume.
 */
final case class SphericalBounds( radius : Double)  extends Bounds {

  def intersects(location: Vector3, otherLocation: Vector3, otherBounds: Bounds) = {
    otherBounds match {
      case other : SphericalBounds =>
        location.distanceSquared( otherLocation ) <= radius*radius + other.radius*other.radius
    }

  }
}
