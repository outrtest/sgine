package org.sgine.core

sealed trait Face extends Enum

object Face extends Enumerated[Face] {
	case object None extends Face
	case object Front extends Face
	case object Back extends Face
	case object Both extends Face
	
	Face(None, Front, Back, Both)
}