package org.sgine.input.event

import org.sgine.core.Enum
import org.sgine.core.Enumerated

sealed class KeyState extends Enum

object KeyState extends Enumerated[KeyState] {
	case object Pressed extends KeyState
	case object Released extends KeyState
	case object Typed extends KeyState
	
	KeyState(Pressed, Released, Typed)
}