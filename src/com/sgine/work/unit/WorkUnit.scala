package com.sgine.work.unit

import com.sgine.work._;

trait WorkUnit {
	private var _enqueued:Long = _;
	private var _started = -1L;
	
	def enqueue() = {
		_enqueued = WorkManager.time;
		_started = -1;
	}
	
	def getEnqueued = _enqueued;
	
	def started() = {
		_started = WorkManager.time;
	}
	
	def getStarted = _started;
}