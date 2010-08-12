package org.sgine.db

import java.util.concurrent.locks.ReentrantLock

import org.sgine.log._

import org.sgine.util.Time

import org.sgine.work.Updatable

/**
 * ObjectCache maintains a persistent cache of objects that are retrieved from
 * another location. This can be useful for remote data that takes a long time
 * to query, or data that is built up over time.
 * 
 * Data is persisted to a DB and refreshed against the refreshFunction at the
 * specified refreshRate. The refreshFunction is responsible for updating the
 * DB with the most correct information and committing when complete.
 * 
 * Reading data from the cache can be done via the "read" ReadTransaction visible
 * in the ObjectCache.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ObjectCache[T](val name: String, val db: DB, refreshFunction: (Transaction) => Unit, val refreshRate: Double = Time.Hour) extends Updatable {
	val read = db.readOnlyTransaction()
	
	private val write = db.transaction()
	private val lock = new ReentrantLock()
	@volatile private var lastRefresh = 0.0
	
	refresh()
	initUpdatable()
	
	def refresh() = {
		lock.lock()
		try {
			info("Began manual refresh of ObjectCache %1s", args = List(name))
			refreshInternal()
			info("Finished manual refresh of ObjectCache %1s", args = List(name))
		} finally {
			lock.unlock()
		}
	}
	
	def tryRefresh() = tryRefreshInternal(true)
	
	private def tryRefreshInternal(manual: Boolean) = {
		val s = if (manual) "try manual" else "automatic"
		if (lock.tryLock()) {
			try {
				info("Began %1s refresh of ObjectCache %2s", args = List(s, name))
				refreshInternal()
				info("Finished %1 refresh of ObjectCache %2s", args = List(s, name))
			} finally {
				lock.unlock()
			}
			true
		} else {
			info("ObjectCache %1s cannot be %2s refreshed as it is currently locked.", args = List(name, s))
			false
		}
	}
	
	private def refreshInternal() = {
		refreshFunction(write)
		lastRefresh = 0.0
	}
	
	override def update(time: Double) = {
		lastRefresh += time
		if (lastRefresh > refreshRate) {
			tryRefreshInternal(false)
		}
	}
}