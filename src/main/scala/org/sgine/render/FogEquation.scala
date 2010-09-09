package org.sgine.render

import org.sgine.core.Enumeration
import org.sgine.core.Enumerated

sealed trait FogEquation extends Enumeration

object FogEquation extends Enumerated[FogEquation] {
	case object Linear extends FogEquation
	case object Exp extends FogEquation
	case object Exp2 extends FogEquation
	
	FogEquation(Linear, Exp, Exp2)
}