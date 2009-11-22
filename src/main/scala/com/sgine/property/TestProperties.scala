package com.sgine.property

import com.sgine._;
import com.sgine.property.adjust._;
import com.sgine.work._;
import com.sgine.work.unit._;

object TestProperties {
	val p = new MutableProperty[Int] with ChangeableProperty[Int] {
		def changed(oldValue:Int, newValue:Int):Unit = println("Changed: " + oldValue + " to " + newValue);
	}
	val p2 = new MutableProperty[Int] with ListenableProperty[Int];
	val p3 = new MutableProperty[Double] with AdjustableProperty[Double];
	
	def main(args:Array[String]) = {
		p(5);
		p(8, false);
		p(10);
		
		p2.listeners.add(p2Changed);
		p2(2);
		p2(4);
		
		p3 := 0.0;
		val updater = new Updater(p3);
		DefaultWorkManager += new RepeatingUnit(updater);
		p3.adjuster = new LinearNumericAdjuster(5.0);
		p3 := 50.0;
		println("Current: " + p3());
		Thread.sleep(1000);
		println("Delayed1: " + p3());
		Thread.sleep(1000);
		println("Delayed2: " + p3());
	}
	
	def p2Changed(p:Property[Int], oldValue:Int, newValue:Int):Unit = {
		println("p2Changed: " + oldValue + " to " + newValue);
	}
}