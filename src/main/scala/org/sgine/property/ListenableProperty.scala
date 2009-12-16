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
	abstract override def changed(oldValue:T, newValue:T):Unit = {
		super.changed(oldValue, newValue)
		
		var adjusting = false
		if (this.isInstanceOf[AdjustableProperty[_]]) {
			val ap = this.asInstanceOf[AdjustableProperty[T]]
			if (ap.target != newValue) {
				adjusting = true
			}
		}
		Event.enqueue(new PropertyChangeEvent(this, oldValue, newValue, adjusting))
	}
}
