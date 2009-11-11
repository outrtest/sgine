package com.sgine.property.adjust

import com.sgine.Updatable;
import com.sgine.property.convert._;
import com.sgine.property._;

trait PropertyAdjuster[T] extends Modifiable[T] with Updatable {
	protected var current:T = _;
	protected var target:T = _;
	
	var animator = linearAnimator(5.0, _:Double);
	val toDouble:Function1[T, Double]
	val fromDouble:Function1[Double, T]
	
	abstract override def modify(value:T):T = {
		var v = super.modify(value);
		
		target = v;
		
		current;
	}
	
//	def adjust(source:T, destination:T, percentage:Double):T
	
	def update(time:Double) = {
		if (current != target) {
			animator(time);
		}
	}
	
	def linearAnimator(multiplier:Double, time:Double):Unit = {
		val c = toDouble(current);
		val t = toDouble(target);
		val d = time * multiplier;
		var n = c;
		if (c < t) {
			n += d;
			if (n > t) {
				n = t;
			}
		} else if (c > t) {
			n -= d;
			if (n < t) {
				n = t;
			}
		}
		println(n);
	}
}