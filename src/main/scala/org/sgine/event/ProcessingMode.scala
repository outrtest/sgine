package org.sgine.event

import org.sgine.core.Enumerated
import org.sgine.core.Enumeration

sealed trait ProcessingMode extends Enumeration

object ProcessingMode extends Enumerated[ProcessingMode] {
	/**
	 * Happens in the same thread that caused the event to occur and happens immediately
	 * blocking the event queue from processing anything else until processing completes.
	 * This should never be used for any functionality that takes much time to complete.
	 */
    case object Blocking extends ProcessingMode
	
	/**
	 * Runs completely asynchronously from all other events. The event is processed within
	 * a processing thread and does not interfere with any other events that would otherwise
	 * be waiting on a specific component.
	 */
	case object Asynchronous extends ProcessingMode
	
	/**
	 * Blocks events on the same component from coming in, but does not block any other facet
	 * of the application operation. This is the default operation.
	 */
	case object Normal extends ProcessingMode

    ProcessingMode(Blocking, Asynchronous, Normal)
}