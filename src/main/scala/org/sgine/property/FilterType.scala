package org.sgine.property

import org.sgine.core.Enumeration
import org.sgine.core.Enumerated

sealed trait FilterType extends Enumeration

object FilterType extends Enumerated[FilterType] {
	case object Modify extends FilterType
	case object Retrieve extends FilterType
	
	FilterType(Modify, Retrieve)
}