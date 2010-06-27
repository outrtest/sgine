package org.sgine.property

import org.sgine.event._

import org.sgine.property.container._

import java.util.concurrent._

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
	
	override def parent = _parent
	
	protected def parent_=(_parent: Listenable) = this._parent = _parent
	
	abstract override def changed(oldValue:T, newValue:T):Unit = {
		super.changed(oldValue, newValue)
		
		var adjusting = false
		if (this.isInstanceOf[AnimatingProperty[_]]) {
			val ap = this.asInstanceOf[AnimatingProperty[T]]
			if (ap.target != newValue) {
				adjusting = true
			}
		}
//		println("PropertyChanged: " + this + " - " + oldValue + " to " + newValue)
		Event.enqueue(PropertyChangeEvent(this, oldValue, newValue, adjusting))
	}
}
