package com.sgine.work

import java.util.concurrent._;

object TestPerformance extends Function0[Unit] {
	var count = 0;
	var iterations = 10000000;
	
	def main(args:Array[String]) = {
		val time = System.nanoTime();
		
		DefaultWorkManager += this;
		
		while (!isDone) {
			Thread.sleep(1);
		}
		
		println("Completed work: " + iterations + " in " + (TimeUnit.MILLISECONDS.convert(System.nanoTime() - time, TimeUnit.NANOSECONDS) / 1000.0f) + " seconds, Threads: " + DefaultWorkManager.threadCount);
	}
	
	def apply() = {
		count += 1;
		
		if (count < iterations) {
			DefaultWorkManager += this;
		}
	}
	
	def isDone = count >= iterations;
}