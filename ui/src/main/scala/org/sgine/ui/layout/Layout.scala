package org.sgine.ui.layout

import org.sgine.ui.AbstractContainer

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Layout {
  def layout(container: AbstractContainer): Unit
}
