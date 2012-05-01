package org.sgine.ui

import align.{HorizontalAlignment, VerticalAlignment}
import annotation.tailrec
import layout.Layout

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Grid(rows: Int, columns: Int, cellWidth: Double, cellHeight: Double) extends Container with Layout {
  _layout := this
  size.algorithm := null
  size(columns * cellWidth, rows * cellHeight)
  resolution(size.width(), size.height(), false)
  location(scale.x() * (-size.width() / 2.0), scale.y() * (size.height() / 2.0))

  private var grid = Map.empty[Component, GridElement]

  protected def layoutContainer(container: AbstractContainer) = {
    layoutComponents(container.contents)
  }

  protected def lookupGridElement(component: Component) = synchronized {
    grid.getOrElse(component, generateGridElement(component))
  }

  protected def generateGridElement(component: Component) = synchronized {
    val e = GridElement(component, 0, 0, 1, 1)
    grid += component -> e
    e
  }

  @tailrec
  private def layoutComponents(components: Seq[Component]): Unit = {
    if (components.nonEmpty) {
      val c = components.head
      val e = lookupGridElement(c)
      updateElement(e)
      layoutComponents(components.tail)
    }
  }

  protected def updateElement(element: GridElement) = {
    element.component.location(columnToX(element.column), rowToY(element.row))
    element.component.alignment.vertical := VerticalAlignment.Top
    element.component.alignment.horizontal := HorizontalAlignment.Left
  }

  def rowToY(row: Int) = -row * cellHeight

  def columnToX(column: Int) = column * cellWidth

  def move(component: Component, x: Int = 0, y: Int = 0) = {
    val e = lookupGridElement(component)
    set(component, e.row + y, e.column + x)
  }

  def moveLeft(component: Component, cells: Int = 1) = {
    move(component, x = -cells)
  }

  def moveRight(component: Component, cells: Int = 1) = {
    move(component, x = cells)
  }

  def moveUp(component: Component, cells: Int = 1) = {
    move(component, y = -cells)
  }

  def moveDown(component: Component, cells: Int = 1) = {
    move(component, y = cells)
  }

  def set(component: Component, row: Int, column: Int) = synchronized {
    if (column < columns && row < rows && column >= 0 && row >= 0) {
      val e = lookupGridElement(component).copy(row = row, column = column)
      grid += component -> e
      updateElement(e)
      true
    } else {
      false
    }
  }
}

case class GridElement(component: Component, row: Int, column: Int, rowSpan: Int, colSpan: Int)