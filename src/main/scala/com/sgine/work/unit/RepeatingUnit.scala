package com.sgine.work.unit

trait RepeatingUnit extends WorkUnit {
	var unit:Function0[Unit];
	
	abstract override def apply():Unit = {
		super.apply();
		
		if (unit != null) {
			unit();
			
			workManager += this;			// Re-queue
		}
	}
}