package org.sgine.process.updatable

trait CountedUpdatable extends Updatable {
	@volatile private var updateCount = 0
	
	def count: Int
	
	abstract override protected def afterUpdated() = {
		super.afterUpdated()
		
		updateCount += 1
		if (count != -1 && updateCount >= count) die()
	}
}