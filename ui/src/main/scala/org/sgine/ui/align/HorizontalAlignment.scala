package org.sgine.ui.align

import org.sgine.{Enumerated, EnumEntry}

/**
 * HorizontalAlignment of components.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class HorizontalAlignment extends EnumEntry[HorizontalAlignment]

object HorizontalAlignment extends Enumerated[HorizontalAlignment] {
  val Left = new HorizontalAlignment
  val Center = new HorizontalAlignment
  val Right = new HorizontalAlignment
}