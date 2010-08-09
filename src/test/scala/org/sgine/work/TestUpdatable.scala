package org.sgine.work

import org.sgine.log._

object TestUpdatable {
	def main(args: Array[String]): Unit = {
		val u = new TestUpdatable()
		info("Starting...")
		Thread.sleep(5000)
		info("Stopping...")
	}
}

class TestUpdatable extends Updatable {
	initUpdatable()
	
	override def update(time: Double) = {
		info("Update! " + time)
		Thread.sleep(10)
	}
}