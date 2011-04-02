package org.sgine.bus

import org.sgine.core.Enum
import org.sgine.core.Enumerated

sealed trait Routing extends Enum

object Routing extends Enumerated[Routing] {
	case object Continue extends Routing
	case object Stop extends Routing
	
	Routing(Continue, Stop)
}