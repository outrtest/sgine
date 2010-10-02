package org.sgine.work

import org.sgine.log._

object TestWorkManager {
	def main(args: Array[String]): Unit = {
		DefaultWorkManager += test _
		
		Thread.sleep(1000000)
	}
	
	def test(): Unit = {
		info("Testing!")
	}
}