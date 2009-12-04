package org.sgine.work.unit

import java.lang.ref._;

class RepeatingUnit(unitRef:WeakReference[Function0[Unit]]) extends WorkUnit {
	
	def this(_unit:Function0[Unit]) = {
		this(new WeakReference(_unit));
	}
	
	def unit = unitRef.get();
	
	override def apply():Unit = {
		super.apply();
		
		if (unitRef != null) {
			if (unit == null) {
				return;					// Unit has lost its reference, so stop repeating
			}
			unit();
		}
		workManager += this;			// Re-queue
	}
}