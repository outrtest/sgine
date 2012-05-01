package org.sgine.ui.layout

import org.sgine.ui.AbstractContainer

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Layout {
  protected def layoutContainer(container: AbstractContainer): Unit
}

object Layout {
  def apply(layout: Layout, container: AbstractContainer) = layout.layoutContainer(container)
}