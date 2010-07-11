package org.sgine.render

import org.sgine.core.Enumeration
import org.sgine.core.Enumerated

sealed trait Face extends Enumeration

object Face extends Enumerated[Face] {
	case object None extends Face
	case object Front extends Face
	case object Back extends Face
	case object Both extends Face
}