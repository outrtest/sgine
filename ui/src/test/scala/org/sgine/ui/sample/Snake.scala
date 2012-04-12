package org.sgine.ui.sample

import org.sgine.property.Property
import org.sgine.ui.{ShapeComponent, Container, UI}
import org.sgine.ui.render.Vertex
import org.sgine.{Color, Compass}
import org.sgine.ui.align.{VerticalAlignment, HorizontalAlignment}
import org.sgine.input.Key

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Snake extends UI {
  val resolutionWidth = 1500.0
  val resolutionHeight = 1200.0

  val direction = Property[Compass](Compass.East)

  Key.values.foreach(println)
  println(Key.values.size)

//  Keyboard.listeners.synchronous {
//    case event: KeyDownEvent => event.key match {
//      case Key.Up => direction := Compass.North
//      case Key.Down => direction := Compass.South
//      case Key.Right => direction := Compass.East
//      case Key.Left => direction := Compass.West
//      case _ => // Ignore
//    }
//  }

  perspective()
  resolution(resolutionWidth, resolutionHeight, false)

  val square = new Square()
  square.location(resolutionWidth / 2.0, -resolutionHeight / 2.0)

  val container = new Container {
    location.x := -(resolutionWidth / 2.0)
    location.y := (resolutionHeight / 2.0)

    contents += square
  }
  contents += container

  updates.every(0.2) {
    case delta => square.move(direction())
  }
}

class Square() extends ShapeComponent {
  val squareSize = 50.0
  private var followedBy: Square = _

  def after(color: Color): Unit = followedBy match {
    case null => {
      followedBy = new Square()
      followedBy.location(location.x(), location.y())
    }
    case square => square.after(color)
  }

  _vertices := Vertex.rect(squareSize, squareSize)

  size(squareSize, squareSize)
  color(Color.Red)
  alignment.horizontal := HorizontalAlignment.Left
  alignment.vertical := VerticalAlignment.Top

  def move(direction: Compass) = {
    if (followedBy != null) {
      followedBy.location(location.x(), location.y())
    }
    direction match {
      case Compass.North => location.y += squareSize
      case Compass.South => location.y -= squareSize
      case Compass.East => location.x += squareSize
      case Compass.West => location.x -= squareSize
    }
  }
}