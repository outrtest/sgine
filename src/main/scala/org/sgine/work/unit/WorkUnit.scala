package org.sgine.work.unit

import org.sgine.work._;

trait WorkUnit extends Function0[Unit] {
	private var _enqueued:Long = _;
	private var _started = -1L;
	var workManager:WorkManager = _;
	var workThread:WorkThread = _;
	
	def enqueue() = {
		_enqueued = WorkManager.time;
		_started = -1;
	}
	
	def getEnqueued = _enqueued;
	
	def started() = _started = WorkManager.time
	
	def getStarted = _started;
	
	def apply(): Unit = {
	}
}