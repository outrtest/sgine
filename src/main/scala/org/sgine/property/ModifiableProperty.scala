package org.sgine.property

import org.sgine.core.Modifiable

import org.sgine.work.Updatable

trait ModifiableProperty[T] extends ChangeableProperty[T] with Updatable {
	private var lastModified = -1L
	
	initUpdatable()
	
	abstract override def update(time: Double) = {
		super.update(time)
		
		apply() match {
			case m: Modifiable => {
				if (m.lastModified != lastModified) {
					changed(false)
				}
			}
			case _ =>
		}
	}
	
	abstract override def changed(equalityCheck: Boolean = true) = {
		apply() match {
			case m: Modifiable => lastModified = m.lastModified
			case _ =>
		}
		
		super.changed(equalityCheck)
	}
}