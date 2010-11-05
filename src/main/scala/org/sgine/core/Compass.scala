package org.sgine.core

sealed class Compass extends Enum {
	def opposite = {
		if (ordinal > 1) {
			Compass(ordinal - 2)
		} else {
			Compass(ordinal + 2)
		}
	}
}

object Compass extends Enumerated[Compass] {
	case object North extends Compass
	case object East extends Compass
	case object South extends Compass
	case object West extends Compass
	
	Compass(North, East, South, West)
}