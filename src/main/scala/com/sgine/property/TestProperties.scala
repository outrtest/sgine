package com.sgine.property

object TestProperties {
	def main(args:Array[String]) = {
		val p = new MutableProperty[Int] with Testing[Int];
		p(5);
		p(10);
		println("Value: " + p());
	}
}

trait Testing[T] extends Changeable[T] {
	abstract override def changed(oldValue:T, newValue:T) = {
		super.changed(oldValue, newValue);
		println("Changed: " + oldValue + ", " + newValue);
	}
}