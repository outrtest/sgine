package org.sgine.work.unit

import java.lang.ref._;

class RepeatingUnit(unitRef:WeakReference[Function0[Unit]]) extends WorkUnit {
	protected var keepAlive = true
	
	def this(_unit:Function0[Unit]) = {
		this(new WeakReference(_unit));
	}
	
	def unit = unitRef.get();
	
	override def apply():Unit = {
		super.apply();
		
		while (keepAlive) {
			if (unitRef != null) {
				if (unit == null) {
					keepAlive = false;					// Unit has lost its reference, so stop repeating
				} else {
					unit();
				}
			}
			Thread.sleep(1)
			Thread.`yield`()
		}
	}
}

object RepeatingUnit {
	def apply(unit: () => Unit) = new RepeatingUnit(unit)
}