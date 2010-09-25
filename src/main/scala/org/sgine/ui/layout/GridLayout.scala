package org.sgine.ui.layout

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingBox

import org.sgine.core.HorizontalAlignment
import org.sgine.core.VerticalAlignment

import org.sgine.event.Event

import org.sgine.log._

import org.sgine.scene.Node

import org.sgine.ui.Component
import org.sgine.ui.Container

class GridLayout private(val rows: Int, val columns: Int, val spacing: Int, val itemWidth: Double, val itemHeight: Double) extends Layout {
	val width = (columns * itemWidth) + ((columns - 1) * spacing)
	val height = (rows * itemHeight) + ((rows - 1) * spacing)
	
	private var items: List[GridItem] = Nil
	
	def apply(container: Container) = {
		synchronized {
			// Make sure everything is configured
			if (container.children.size != items.size) {
				// Add missing
				for (n <- container.children) {
					if (get(n) == None) {
						nextAvailable match {
							case Some((row, column)) => {
								info("Node not already in layout, specifying: " + n + " at " + row + "x" + column)
								apply(n, row, column)
							}
							case None => warn("No more room found in GridLayout")
						}
					}
				}
				// Remove no longer used
				for (item <- items) {
					if (container.children.indexOf(item.n) == -1) {
						items = items filterNot (i => item == i)
					}
				}
			}
			
			// Lay out each item
			for (item <- items) {
				layout(item)
			}
			
			container.size(width, height)
		}
	}
	
	def apply(row: Int, column: Int) = items.find(item => item.row == row && item.column == column) match {
		case Some(i) => i.n
		case None => None
	}
	
	def apply(n: Node, row: Int, column: Int) = {
		synchronized {
			if (row >= rows) throw new IndexOutOfBoundsException("Specified row exceeds row count. Specified: " + row + ", Max: " + rows)
			if (column >= columns) throw new IndexOutOfBoundsException("Specified column exceeds column count. Specified: " + column + ", Max: " + columns)
			
			val item = getInternal(n) match {
				case Some(i) => i
				case None => {
					val i = GridItem(n, row, column)
					items = i :: items
					i
				}
			}
			item.row = row
			item.column = column
			
			layout(item)
		}
	}
	
	def nextAvailable = (0 until rows * columns) find(value => {
		val (row, column) = rc(value)
		!isUsed(row, column)
	}) match {
		case Some(value) => Some(rc(value))
		case None => None
	}
	
	private def rc(value: Int) = (value / columns, value % columns)
	
	def isUsed(row: Int, column: Int) = items.find(item => item.row == row && item.column == column) != None
	
	def check(row: Int, column: Int) = items.find(item => item.row == row && item.column == column) match {
		case Some(item) => Some(item.n)
		case None => None
	}
	
	private def layout(item: GridItem) = {
		val offsetX = (item.column * (itemWidth + spacing)) - (width / 2.0)
		val offsetY = (-item.row * (itemHeight + spacing)) - (height / -2.0)
		item.n match {
			case c: Component => {
				c.location.x.align := HorizontalAlignment.Left
				c.location.y.align := VerticalAlignment.Top
				c.location(offsetX, offsetY)
			}
		}
	}
	
	def get(n: Node) = getInternal(n) match {
		case Some(item) => Some((item.row, item.column))
		case None => None
	}
	
	private def getInternal(n: Node) = items.find(item => item.n == n)
}

private case class GridItem(n: Node, var row: Int, var column: Int)

object GridLayout {
	def apply(rows: Int, columns: Int, spacing: Int, itemWidth: Double, itemHeight: Double) = {
		new GridLayout(rows, columns, spacing, itemWidth, itemHeight)
	}
}