package com.sgine.space

import com.sgine.math.Vector3

/**
 * Some object that is located at some point in a Space.
 */
// TODO: Use properties for position and bounds? -> allows changing and listening
trait Node extends Region {

  def position : Vector3

  def bounds : Bounds

}
