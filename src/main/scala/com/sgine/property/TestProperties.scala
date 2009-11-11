package com.sgine.property

import com.sgine._;
import com.sgine.work._;
import com.sgine.work.unit._;
import com.sgine.property.adjust._;

object TestProperties {
	val p = new MutableProperty[Double] with DoubleAdjuster;
	
	def main(args:Array[String]) = {
//		val p = new MutableProperty[Int] with Testing[Int];
//		p(2);
//		p(4);
//		println("Value: " + p());
		
		val u = new Updater(p);
		val ut = new RepeatingUnit(u);
		DefaultWorkManager += ut;
		
		p(20);
		
		println("Sleeping...");
		Thread.sleep(4000);
		println("Awake!");
	}
	
	var count = 0;
	
	def test() = {
		count += 1;
		println("Repeating! " + count);
	}
}

trait Testing[T] extends Changeable[T] {
	abstract override def changed(oldValue:T, newValue:T) = {
		super.changed(oldValue, newValue);
		println("Changed: " + oldValue + ", " + newValue);
	}
}