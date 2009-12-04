package org.sgine.property

import java.util.concurrent._

import scala.collection.JavaConversions._

/**
 * ListenableProperty trait extends ChangeableProperty trait to
 * provide <code>listeners</code> to allow multiple sources to
 * monitor the changes the Property undergoes.
 * 
 * @author Matt Hicks
 */
trait ListenableProperty[T] extends ChangeableProperty[T] {
	
	val listeners = new ConcurrentLinkedQueue[Function3[Property[T], T, T, Unit]];
	
	abstract override def changed(oldValue:T, newValue:T):Unit = {
		super.changed(oldValue, newValue)
		
		listeners.foreach(_(this, oldValue, newValue))
	}
}
