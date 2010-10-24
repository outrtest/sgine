package org.sgine.core

sealed class HorizontalAlignment extends Enum

object HorizontalAlignment extends Enumerated[HorizontalAlignment] {
   case object Left extends HorizontalAlignment
   case object Center extends HorizontalAlignment
   case object Right extends HorizontalAlignment
   HorizontalAlignment(Left, Center, Right)
}