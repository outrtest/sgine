package org.sgine.work.unit

import java.util.concurrent._

abstract class BlockingWorkUnit(blocker: AnyRef) extends DependentUnit with FinishedUnit {
	def isReady() = BlockingWorkUnit.block(blocker)
	
	def finished() = {
		BlockingWorkUnit.unblock(blocker)
	}
}

object BlockingWorkUnit {
	private val blocking = new ConcurrentLinkedQueue[Any]()
	
	def block(blocker: AnyRef): Boolean = {
		if (blocker != null) {
			blocker.synchronized {
				if (blocking.contains(blocker)) {
					return false		// Already blocked
				}
				blocking.add(blocker)
			}
		}
		true		// No blocker, so it's ready
	}
	
	def unblock(blocker: AnyRef) = {
		if (blocker != null) {
			blocker.synchronized {
				blocking.remove(blocker)
			}
		}
	}
}