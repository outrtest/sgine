package org.sgine.event

object Recursion extends Enumeration {
	type Recursion = Value
	
	/**
	 * Listener is invoked when events occur only on the Listenable
	 * it is attached to.
	 */
	val None = Value
	
	/**
	 * Listener is invoked when events occur on the Listenable it is
	 * attached to and all of its parents.
	 */
	val Parents = Value
	
	/**
	 * Listener is invoked when events occur on the Listenable it is
	 * attached to and all of its children. Note that the Listenable
	 * must implement Iterable for this to have any effect.
	 */
	val Children = Value
	
	/**
	 * Listener is invoked when events occur on the Listenable it is
	 * attached to, all of its children, and all of its parents. This
	 * is a combination effect of Parents and Children.
	 */
	val All = Value
}