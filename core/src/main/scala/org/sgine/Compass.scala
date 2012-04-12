package org.sgine

/**
 * Compass represents the points on a compass as an enum.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class Compass(override val ordinal: Int) extends EnumEntry[Compass]

object Compass extends Enumerated[Compass] {
  val North = new Compass(0)
  val South = new Compass(1)
  val East = new Compass(2)
  val West = new Compass(3)
}