package org.sgine.core

sealed trait Placement extends Enum

object Placement extends Enumerated[Placement] {
  case object Top extends Placement
  case object Bottom extends Placement
  case object Left extends Placement
  case object Right extends Placement
  case object Middle extends Placement

  Placement(Top, Bottom, Left, Right, Middle)
}
