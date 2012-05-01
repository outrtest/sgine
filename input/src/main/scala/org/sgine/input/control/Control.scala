package org.sgine.input.control

import org.sgine.{Enumerated, EnumEntry}


/**
 * Controls allow an abstraction from the input device and pushed events.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Control {
  def name: String
  def value: Double
  def style: ControlStyle
}

sealed class ControlStyle(min: Double, rest: Double, max: Double, axis: Boolean) extends EnumEntry[ControlStyle]

object ControlStyle extends Enumerated[ControlStyle] {
  /**
   * Toggle represents a button or key where the value is either on or off (1.0 or 0.0)
   */
  val Toggle = new ControlStyle(0.0, 0.0, 1.0, false)
  /**
   * SingleAxis represents a pressure sensitive control where the value is a range between 0.0 and 1.0.
   */
  val SingleAxis = new ControlStyle(0.0, 0.0, 1.0, true)
  /**
   * MultiAxis represents a two-direction pressure sensitive control where the value is 0.0 at rest and may move up to
   * 1.0 or down to -1.0.
   */
  val MultiAxis = new ControlStyle(-1.0, 0.0, 1.0, true)
}