package org.sgine.work

object TestUpdatable {
	def main(args: Array[String]): Unit = {
		val u = new TestUpdatable()
		println("Starting...")
		Thread.sleep(5000)
		println("Stopping...")
	}
}

class TestUpdatable extends Updatable {
	initUpdatable()
	
	def update(time: Double) = {
		println("Update! " + time)
		Thread.sleep(10)
	}
}