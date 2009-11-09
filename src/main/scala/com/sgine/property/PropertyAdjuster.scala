package com.sgine.property

import com.sgine.Updatable;

trait PropertyAdjuster[T] extends Modifiable[T] with Updatable {
	protected var current:T
	protected var target:T
	
	var animator = linearAnimator(5.0, _:Double);
	var toDouble:Function1[T, Double] = null;
	var fromDouble:Function1[Double, T] = null;
	
	abstract override def modify(value:T):T = {
		var v = super.modify(value);
		
		target = v;
		
		current;
	}
	
	def adjust(source:T, destination:T, percentage:Double):T
	
	abstract override def update(time:Double) = {
		super.update(time);
		
		if (current != target) {
			if (toDouble == null) {
				// TODO: find valid function
			}
			if (fromDouble == null) {
				// TODO: find valid function
			}
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
	}
}