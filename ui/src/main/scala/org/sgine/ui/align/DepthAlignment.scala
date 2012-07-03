package org.sgine.ui.align

import org.powerscala.{Enumerated, EnumEntry}


/**
 * DepthAlignment is the depth alignment assigned to a component.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class DepthAlignment extends EnumEntry[DepthAlignment]

object DepthAlignment extends Enumerated[DepthAlignment] {
  val Front = new DepthAlignment
  val Middle = new DepthAlignment
  val Back = new DepthAlignment
}