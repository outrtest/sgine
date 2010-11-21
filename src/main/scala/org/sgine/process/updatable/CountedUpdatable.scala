package org.sgine.process.updatable

trait CountedUpdatable extends Updatable {
	@volatile private var updateCount = 0
	
	def count: Int
	
	abstract override protected def updated() = {
		super.updated()
		
		updateCount += 1
		if (count != -1 && updateCount >= count) die()
	}
}