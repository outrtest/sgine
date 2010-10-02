package org.sgine.work.unit;

trait TimeoutUnit extends WorkUnit {
	private var _timeout:Long = _;
	
	def timeout_=(_timeout:Long) = {
		this._timeout = _timeout;
	}
	
	def timeout = _timeout;
}