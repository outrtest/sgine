package org.sgine.core

sealed abstract class Precision(val conversion: Double, f: () => Long) extends Enum {
	def time = f()
}

object Precision extends Enumerated[Precision] {
	var Default = Precision.Nanoseconds
	
	case object Milliseconds extends Precision(1000.0, () => System.currentTimeMillis)
	case object Nanoseconds extends Precision(1000000000.0, () => System.nanoTime)
	
	Precision(Milliseconds, Nanoseconds)
}