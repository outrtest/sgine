package org.sgine.ui.layout

import org.sgine.core._

import org.sgine.ui.AbstractContainer
import org.sgine.ui.Component

import scala.math._

class VerticalBoxLayout private(val spacing: Double,
								val alignment: HorizontalAlignment,
								val offset: Double,
								val reverse: Boolean) extends Layout {
	def apply(container: AbstractContainer) = {
		// Determine size for offest
		var items = 0
		var width = 0.0
		var height = 0.0
		var depth = 0.0
		for (n <- container.children) n match {
			case c: Component if (c.includeInLayout()) => {
				items += 1
				width = max(width, c.size.actual.width() + offset)
				if (items > 0) {		// Add spacing
					height += spacing
				}
				height += c.size.actual.height()
				depth = max(depth, c.size.actual.depth())
			}
			case _ => // Ignore non-components
		}
		
		// Update positions
		var position = height / 2.0
		if (reverse) {
			position = -position
		}
		for (n <- container.children) n match {
			case c: Component if (c.includeInLayout()) => {
				c.location.x.align := alignment
				c.location.x := offset
				c.location.y.align := (if (reverse) VerticalAlignment.Bottom else VerticalAlignment.Top)
				c.location.y := position
				
				if (reverse) {
					position += c.size.actual.height()
					position += spacing
				} else {
					position -= c.size.actual.height()
					position -= spacing
				}
			}
			case _ => // Ignore non-components
		}
		
		// Update for padding
		width += container.padding.left() + container.padding.right()
		height += container.padding.top() + container.padding.bottom()
		
		// Set the size of the container
		container.size.measured(width, height, depth)
	}
}

object VerticalBoxLayout {
	def apply(spacing: Double = 0.0,
			  alignment: HorizontalAlignment = HorizontalAlignment.Center,
			  offset: Double = 0.0,
			  reverse: Boolean = false) = {
		new VerticalBoxLayout(spacing, alignment, offset, reverse)
	}
}