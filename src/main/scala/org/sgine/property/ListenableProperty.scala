package org.sgine.property

import org.sgine.event._

import java.util.concurrent._

import scala.collection.JavaConversions._

/**
 * ListenableProperty trait extends ChangeableProperty trait to
 * provide <code>listeners</code> to allow multiple sources to
 * monitor the changes the Property undergoes.
 * 
 * @author Matt Hicks
 */
trait ListenableProperty[T] extends ChangeableProperty[T] with Listenable {
	val parent = null
	val listeners = new EventProcessor(this)
	
	abstract override def changed(oldValue:T, newValue:T):Unit = {
		super.changed(oldValue, newValue)
		
		Event.enqueue(new PropertyChangeEvent(this, oldValue, newValue))
	}
}
