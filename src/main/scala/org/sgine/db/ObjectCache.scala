package org.sgine.db

import java.util.concurrent.locks.ReentrantLock

import org.sgine.log._

import org.sgine.util.Time
import org.sgine.util.Refreshable

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
class ObjectCache(val name: String, val db: DB, cacheFunction: (Transaction) => Unit, val refreshRate: Double = Time.Hour) extends Refreshable {
	val read = db.readOnlyTransaction()
	
	private val write = db.transaction()
	
	protected val refreshFunction = () => cacheFunction(write)
}