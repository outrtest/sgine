package org.sgine.event

import org.sgine.core.Enumerated
import org.sgine.core.Enumeration

sealed trait Recursion extends Enumeration

object Recursion extends Enumerated[Recursion] {
	/**
	 * Listener is invoked when events occur only on the Listenable
	 * it is attached to.
	 */
	case object None extends Recursion
	
	/**
	 * Listener is invoked when events occur on the Listenable it is
	 * attached to and all of its parents.
	 */
	case object Parents extends Recursion
	
	/**
	 * Listener is invoked when events occur on the Listenable it is
	 * attached to and all of its children. Note that the Listenable
	 * must implement Iterable for this to have any effect.
	 */
	case object Children extends Recursion
	
	/**
	 * Listener is invoked when events occur on the Listenable it is
	 * attached to, all of its children, and all of its parents. This
	 * is a combination effect of Parents and Children.
	 */
	case object All extends Recursion

        Recursion(None, Parents, Children, All)
}