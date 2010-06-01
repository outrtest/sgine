package org.sgine.util

/**
 * Axis aligned mutable two dimensional integer based rectangular area.
 * Used e.g. for keeping track of the area of a picture or raster needing a redraw.
 * 
 * NOTE: This class is not thread safe, access only from one thread at a time.
 */
class DirtyRectangle {

  private var empty = true
  private var minX : Int = 0
  private var minY : Int = 0
  private var maxX : Int = 0
  private var maxY : Int = 0

  def isEmpty = empty
  def getMinX = minX
  def getMinY = minY
  def getMaxX = maxX
  def getMaxY = maxY

  def getWidth = if (empty) 0 else maxX - minX + 1
  def getHeight = if (empty) 0 else maxY - minY + 1

  /**
   * Clean the rectangle to include no points at all.
   */
  def clear() {
    empty = true
    minX = 0
    minY = 0
    maxX = 0
    maxY = 0
  }

  /**
   * Expand the rectangle so that it includes the specified point.
   */
  def includePoint( x : Int, y : Int ) {
    if (empty) {
      minX = x
      minY = y
      maxX = x
      maxY = y
      empty = false
    }
    else {
      minX = minX min x
      minY = minY min y
      maxX = maxX max x
      maxY = maxY max y
    }
  }

  /**
   * Extends this BoundingBox to include the specified area.
   * The order of x1,y1 and x2,y2 does not matter.
   */
  def includeArea( x1 : Int, y1 : Int, x2 : Int, y2 : Int ) {
    if (empty) {
      minX = x1 min x2
      minY = y1 min y2
      maxX = x1 max x2
      maxY = y1 max y2
      empty = false
    }
    else {
      minX = (minX min x1) min x2
      minY = (minY min y1) min y2
      maxX = (maxX max x1) max x2
      maxY = (maxY max y1) max y2
    }
  }

  /**
   * Include the area specified by the continuous elements from offset to offset + length,
   * assuming the total area has the specified width and height.
   */
  def includeElementBlock(offset: Int, length: Int, width: Int, height : Int) {
    if (length > 0) {
      val startX = offset % width
      val startY = offset / width
      val endY = (offset + length - 1) / width
      if (startY == endY) includeArea(startX, startY, startX + length - 1, startY)
      else includeArea(0, startY, width-1, endY)
    }
  }

  /**
   * True if the rectangle overlaps the specified point.
   */
  def overlaps( x : Int, y : Int ) {
    if (empty) return false
    else minX <= x    &&
            x <= maxX &&
         minY <= y    &&
            y <= maxY
  }

}

