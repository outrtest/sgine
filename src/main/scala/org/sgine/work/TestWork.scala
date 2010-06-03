package org.sgine.work

import org.sgine.work.unit._;

object TestWork {
	def main(args:Array[String]) = {
		DefaultWorkManager += TestDependent;
		DefaultWorkManager += testOne _;
		Thread.sleep(15000);
	}
	
	def testOne(): Unit = {
		println("Test One!");
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