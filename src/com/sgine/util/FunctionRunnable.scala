package com.sgine.util

object FunctionRunnable {
	def apply(f:() => Unit) = {
		new Runnable() {
			def run() {
				f();
			}
		}
	}
}
