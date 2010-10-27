package org.sgine.core

sealed class SizeMode extends Enum

object SizeMode extends Enumerated[SizeMode] {
	case object Auto extends SizeMode
	case object Explicit extends SizeMode
	
	SizeMode(Auto, Explicit)
}