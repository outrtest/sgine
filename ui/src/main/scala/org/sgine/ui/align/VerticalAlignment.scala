package org.sgine.ui.align

import org.powerscala.{Enumerated, EnumEntry}

/**
 * VerticalAlignment of a component.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class VerticalAlignment extends EnumEntry[VerticalAlignment]

object VerticalAlignment extends Enumerated[VerticalAlignment] {
  val Top = new VerticalAlignment
  val Middle = new VerticalAlignment
  val Bottom = new VerticalAlignment
}