package org.sgine.bus

import org.sgine.core.Enumeration
import org.sgine.core.Enumerated

sealed trait Routing extends Enumeration

object Routing extends Enumerated[Routing] {
	case object Continue extends Routing
	case object Stop extends Routing
	
	Routing(Continue, Stop)
}