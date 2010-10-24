package org.sgine.render

import org.sgine.core.Enum
import org.sgine.core.Enumerated

sealed trait PolygonMode extends Enum

object PolygonMode extends Enumerated[PolygonMode] {
	case object Point extends PolygonMode
	case object Line extends PolygonMode
	case object Fill extends PolygonMode
	
	PolygonMode(Point, Line, Fill)
}