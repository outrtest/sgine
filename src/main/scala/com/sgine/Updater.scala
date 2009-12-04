package com.sgine

import com.sgine.work._;

class Updater(updatable:Updatable) extends Function0[Unit] {
	private var lastUpdate = WorkManager.time;
	
	def apply():Unit = {
		val t = WorkManager.time;
		val elapsed = (t - lastUpdate) / 1000.0;
		updatable.update(elapsed);
		lastUpdate = t;
	}
}
