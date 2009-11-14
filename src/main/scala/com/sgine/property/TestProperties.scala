package com.sgine.property

import com.sgine._;

object TestProperties {
	val p = new MutableProperty[Int] with ChangeableProperty[Int] {
		def changed(oldValue:Int, newValue:Int):Unit = println("Changed: " + oldValue + " to " + newValue);
	}
	val p2 = new MutableProperty[Int] with ListenableProperty[Int];
	
	def main(args:Array[String]) = {
		p(5);
		p(8, false);
		p(10);
		
		p2.listeners.add(p2Changed);
		p2(2);
		p2(4);
	}
	
	def p2Changed(p:Property[Int], oldValue:Int, newValue:Int):Unit = {
		println("p2Changed: " + oldValue + " to " + newValue);
	}
}