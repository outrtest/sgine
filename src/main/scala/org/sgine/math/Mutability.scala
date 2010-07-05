package org.sgine.math

import org.sgine.core.Enumerated
import org.sgine.core.Enumeration

import org.sgine.math.store.ArrayStore
import org.sgine.math.store.NumericStore

sealed trait Mutability extends Enumeration

object Mutability extends Enumerated[Mutability] {
	case object Immutable extends Mutability
	case object Mutable extends Mutability
	
	var Default = Immutable
	
	Mutability(Immutable, Mutable)
}