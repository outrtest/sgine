package org.sgine.property

import org.sgine.core.Enum
import org.sgine.core.Enumerated

sealed trait FilterType extends Enum

object FilterType extends Enumerated[FilterType] {
	case object Modify extends FilterType
	case object Retrieve extends FilterType
	
	FilterType(Modify, Retrieve)
}