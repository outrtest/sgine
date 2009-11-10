package com.sgine.property

import com.sgine.work._;
import com.sgine.work.unit._;

object TestProperties {
	def main(args:Array[String]) = {
		val p = new MutableProperty[Int] with Testing[Int];
		p(5);
		p(10);
		println("Value: " + p());
		
		val ut = new UpdateTest();
		ut.unit = test;
		DefaultWorkManager += ut;
		
		Thread.sleep(4000);
	}
	
	def test() = {
		println("Repeating!");
	}
}

class UpdateTest extends RepeatingUnit;

trait Testing[T] extends Changeable[T] {
	abstract override def changed(oldValue:T, newValue:T) = {
		super.changed(oldValue, newValue);
		println("Changed: " + oldValue + ", " + newValue);
	}
}