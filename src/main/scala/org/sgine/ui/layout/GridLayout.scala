package org.sgine.ui.layout

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.event.BoundingChangeEvent
import org.sgine.bounding.mutable.BoundingBox

import org.sgine.core.HorizontalAlignment
import org.sgine.core.VerticalAlignment

import org.sgine.event.Event

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer

import org.sgine.ui.ext.LocationComponent

class GridLayout private(val rows: Int, val columns: Int, val spacing: Int, val itemWidth: Double, val itemHeight: Double) extends Layout {
	val width = (columns * itemWidth) + ((columns - 1) * spacing)
	val height = (rows * itemHeight) + ((rows - 1) * spacing)
	
	private var items: List[GridItem] = Nil
	
	def apply(container: NodeContainer) = {
		for (item <- items) {
			layout(item)
		}
		
		// Update bounding of container if necessary
		container match {
			case bo: BoundingObject => bo.bounding() match {
				case bb: BoundingBox => {
					bb.width = width
					bb.height = height
					
					val e = new BoundingChangeEvent(bo, bb)
					Event.enqueue(e)
				}
				case _ =>
			}
			case _ =>
		}
	}
	
	def apply(n: Node, row: Int, column: Int, colspan: Int = 1, rowspan: Int = 1) = {
		val item = get(n) match {
			case Some(i) => i
			case None => {
				val i = GridItem(n, row, column, colspan, rowspan)
				synchronized {
					items = i :: items
				}
				i
			}
		}
		item.row = row
		item.column = column
		item.colspan = colspan
		item.rowspan = rowspan
		
		layout(item)
	}
	
	private def layout(item: GridItem) = {
		val offsetX = (item.column * (itemWidth + spacing)) - (width / 2.0)
		val offsetY = (-item.row * (itemHeight + spacing)) - (height / -2.0)
		item.n match {
			case c: LocationComponent => {
				c.location.x.align := HorizontalAlignment.Left
				c.location.y.align := VerticalAlignment.Top
				c.location.set(offsetX, offsetY)
			}
		}
	}
	
	private def get(n: Node) = items.find(item => item.n == n)
}

private case class GridItem(n: Node, var row: Int, var column: Int, var colspan: Int, var rowspan: Int)

object GridLayout {
	def apply(rows: Int, columns: Int, spacing: Int, itemWidth: Double, itemHeight: Double) = {
		new GridLayout(rows, columns, spacing, itemWidth, itemHeight)
	}
}