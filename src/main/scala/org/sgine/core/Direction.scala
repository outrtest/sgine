package org.sgine.core

sealed trait Direction extends Enum

object Direction extends Enumerated[Direction] {
  case object Vertical extends Direction
  case object Horizontal extends Direction

  Direction(Vertical, Horizontal)
}