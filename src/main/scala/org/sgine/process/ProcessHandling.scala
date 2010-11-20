package org.sgine.process

import org.sgine.core.Enum
import org.sgine.core.Enumerated

sealed trait ProcessHandling extends Enum

/**
 * ProcessHandling represents the way in which a function
 * is handled if all processors are current in-use.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ProcessHandling extends Enumerated[ProcessHandling] {
	/**
	 * An attempt will be made to immediately start this
	 * function in a processor if one is available. If
	 * no processor is available nothing further will be
	 * done and false will be returned.
	 */
	case object Attempt extends ProcessHandling
	/**
	 * An attempt will be made to immediately start this
	 * function in a processor if one is available. If
	 * no processor is available the current thread will
	 * block until it is started.
	 */
	case object Wait extends ProcessHandling
	/**
	 * An attempt will be made to immediately start this
	 * function in a processor if one is available. If
	 * no processor is available the function will be
	 * enqueued to be processed when one is available.
	 */
	case object Enqueue extends ProcessHandling
}