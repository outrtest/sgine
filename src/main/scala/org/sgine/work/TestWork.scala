package org.sgine.work

import org.sgine.work.unit._;

object TestWork {
	def main(args:Array[String]) = {
//		DefaultWorkManager += TestDependent
//		DefaultWorkManager += testOne _
//		Thread.sleep(15000)
		
		DefaultWorkManager.maxThreads = 2
		DefaultWorkManager.process(0 until 20, test)
		println("Finished")
	}
	
	def testOne(): Unit = {
		println("Test One!");
	}
	
	def test(n: Int) = {
		println("Test: " + n)
		Thread.sleep(500)
	}
}

object TestDependent extends DependentUnit {
	val time = System.currentTimeMillis();
	
	def apply() = {
		println("Dependent works!");
	}
	
	def isReady() = {
		if (System.currentTimeMillis() > time + 3000) {
			println("ready!");
			true;
		} else {
//			println("not ready");
			false;
		}
	}
}