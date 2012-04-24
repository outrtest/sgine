package org.sgine.ui.layout

import org.sgine.ui.AbstractContainer
import org.sgine.ui.align.{VerticalAlignment, HorizontalAlignment}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class VerticalLayout(horizontalAlignment: HorizontalAlignment = HorizontalAlignment.Center,
                          spacing: Double = 0.0) extends Layout {
  def layout(container: AbstractContainer) = {
    var offset: Double = 0.0
    container.contents.foreach {
      case component => {
        component.location.y := offset
        component.alignment.horizontal := horizontalAlignment
        component.alignment.vertical := VerticalAlignment.Top
        offset -= component.size.height() + spacing
      }
    }
  }
}
