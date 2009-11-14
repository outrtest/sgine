package com.sgine.property

import java.util.concurrent._;

trait ListenableProperty[T] extends ChangeableProperty[T] {
	import com.sgine.util.JavaConversions.clq2iterable;
	
	val listeners = new ConcurrentLinkedQueue[Function3[Property[T], T, T, Unit]];
	
	def changed(oldValue:T, newValue:T):Unit = {
		listeners.foreach(_(this, oldValue, newValue))
	}
}
