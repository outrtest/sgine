package org.sgine.work

object TestWorkManager {
	def main(args: Array[String]): Unit = {
		DefaultWorkManager += test _
		
		Thread.sleep(1000000)
	}
	
	def test() = {
		println("Testing!")
	}
}