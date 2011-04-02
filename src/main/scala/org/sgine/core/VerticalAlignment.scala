package org.sgine.core

sealed trait VerticalAlignment extends Enum

object VerticalAlignment extends Enumerated[VerticalAlignment] {
   case object Top extends VerticalAlignment
   case object Middle extends VerticalAlignment
   case object Bottom extends VerticalAlignment
   VerticalAlignment(Top, Middle, Bottom)
}