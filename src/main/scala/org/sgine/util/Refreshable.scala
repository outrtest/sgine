package org.sgine.util

import java.util.concurrent.locks.ReentrantLock

import org.sgine.log._

import org.sgine.work.DefaultWorkManager
import org.sgine.work.Updatable

trait Refreshable extends Updatable {
	private val lock = new ReentrantLock()
	@volatile private var lastRefresh = 0.0
	
	def name: String
	def refreshRate: Double
	protected def refreshFunction: () => Unit
	
	def lastRefreshed = lastRefresh
	
	def isRefreshing = lock.isLocked
	
	def start() = {
		initUpdatable()
	}
	
	def refresh() = {
		lock.lock()
		try {
			info("Began manual refresh of %1s", args = List(name))
			refreshInternal()
			info("Finished manual refresh of %1s", args = List(name))
		} finally {
			lock.unlock()
		}
	}
	
	def tryRefresh() = tryRefreshInternal(true)
	
	private def tryRefreshInternal(manual: Boolean) = {
		if (lock.tryLock()) {
			val s = if (manual) "try manual" else "automatic"
			try {
				info("Began %1s refresh of %2s", args = List(s, name))
				refreshInternal()
				info("Finished %1s refresh of %2s", args = List(s, name))
			} finally {
				lock.unlock()
			}
			true
		} else {
			val s = if (manual) "try manually" else "automatically"
			info("%1s cannot be %2s refreshed as it is currently locked.", args = List(name, s))
			false
		}
	}
	
	private def refreshInternal() = {
		refreshFunction()
		lastRefresh = 0.0
	}
	
	override def update(time: Double) = {
		lastRefresh += time
		if (lastRefresh > refreshRate) {
			lastRefresh = 0.0		// Reset refresh either way
			if (!lock.isLocked) {
				DefaultWorkManager += attemptRefresh _
			}
		}
	}
	
	private def attemptRefresh(): Unit = {
		tryRefreshInternal(false)
	}
}