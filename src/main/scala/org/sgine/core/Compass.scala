package org.sgine.core

sealed trait Compass extends Enum {
	def opposite = Compass(if (ordinal > 1) ordinal - 2 else ordinal + 2)
}

object Compass extends Enumerated[Compass] {
	case object North extends Compass
	case object East extends Compass
	case object South extends Compass
	case object West extends Compass
	
	Compass(North, East, South, West)
}