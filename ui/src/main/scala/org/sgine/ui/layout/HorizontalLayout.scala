package org.sgine.ui.layout

import org.sgine.ui.AbstractContainer
import org.sgine.ui.align.{VerticalAlignment, HorizontalAlignment}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class HorizontalLayout(verticalAlignment: VerticalAlignment = VerticalAlignment.Middle,
                            spacing: Double = 0.0) extends Layout {
  def layout(container: AbstractContainer) = {
    val width = container.contents.foldLeft(0.0)((w, c) => if (c.includeInLayout()) {
      w + c.size.width() + spacing
    } else {
      w
    }) - spacing
    var offset: Double = width / -2.0
    container.contents.foreach {
      case component if (component.includeInLayout()) => {
        component.location.x := offset
        component.location.y := 0.0
        component.alignment.vertical := verticalAlignment
        component.alignment.horizontal := HorizontalAlignment.Left
        offset += component.size.width() + spacing
      }
      case _ => // Ignore
    }
  }
}