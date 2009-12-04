package org.sgine.event

object ProcessingMode extends Enumeration {
	type ProcessingMode = Value
	
	/**
	 * Happens in the same thread that caused the event to occur and happens immediately
	 * blocking the event queue from processing anything else until processing completes.
	 * This should never be used for any functionality that takes much time to complete.
	 */
	val Blocking = Value
	
	/**
	 * Runs completely asynchronously from all other events. The event is processed within
	 * a processing thread and does not interfere with any other events that would otherwise
	 * be waiting on a specific component.
	 */
	val Asynchronous = Value
	
	/**
	 * Blocks events on the same component from coming in, but does not block any other facet
	 * of the application operation. This is the default operation.
	 */
	val Normal = Value
}
