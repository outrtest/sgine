package com.sgine.work.unit

import java.lang.ref._;

trait RepeatingUnit extends WorkUnit {
	private var unitRef:WeakReference[Function0[Unit]] = _;
	
	def unit = unitRef.get();
	
	def unit_=(_unit:Function0[Unit]) = {
		unitRef = new WeakReference[Function0[Unit]](_unit);
	}
	
	abstract override def apply():Unit = {
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