package org.sgine.property

import org.sgine.event._

import org.sgine.property.container._

import java.util.concurrent._

import scala.collection.JavaConversions._

import event._

/**
 * ListenableProperty trait extends ChangeableProperty trait to
 * provide <code>listeners</code> to allow multiple sources to
 * monitor the changes the Property undergoes.
 * 
 * @author Matt Hicks
 */
trait ListenableProperty[T] extends ChangeableProperty[T] with Listenable {
	private var _parent: Listenable = _
	
	def parent = _parent
	
	protected def parent_=(_parent: Listenable) = this._parent = _parent
	
	abstract override def changed(oldValue:T, newValue:T):Unit = {
		super.changed(oldValue, newValue)
		
		var adjusting = false
		if (this.isInstanceOf[AdjustableProperty[_]]) {
			val ap = this.asInstanceOf[AdjustableProperty[T]]
			if (ap.target != newValue) {
				adjusting = true
			}
		}
		Event.enqueue(PropertyChangeEvent(this, oldValue, newValue, adjusting))
	}
}
