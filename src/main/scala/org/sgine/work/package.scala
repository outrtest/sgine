package org.sgine

package object work {
	implicit def iterable2ParallelIterable[A](iterable: Iterable[A]) = new ParallelIterable(iterable)
}