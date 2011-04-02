package org.sgine.work

import org.sgine.log._

import org.sgine.work.unit._;

object TestWork {
	def main(args:Array[String]) = {
//		DefaultWorkManager += TestDependent
//		DefaultWorkManager += testOne _
//		Thread.sleep(15000)
		
		DefaultWorkManager.maxThreads = 2
		DefaultWorkManager.process(0 until 20, test)
		log("Finished")
	}
	
	def testOne(): Unit = {
		log("Test One!");
	}
	
	def test(n: Int) = {
		log("Test: " + n)
		Thread.sleep(500)
	}
}

object TestDependent extends DependentUnit {
	val time = System.currentTimeMillis();
	
	def apply() = {
		log("Dependent works!");
	}
	
	def isReady() = {
		if (System.currentTimeMillis() > time + 3000) {
			log("ready!");
			true;
		} else {
			false;
		}
	}
}