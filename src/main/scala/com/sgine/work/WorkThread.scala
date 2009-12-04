package com.sgine.work

import com.sgine.util._;
import com.sgine.work.unit._;

private[work] class WorkThread (workManager:WorkManager) {
	private val thread = new Thread(FunctionRunnable(run));
	private var keepAlive = true;
	
	private var work:() => Unit = _;
	@volatile
	private var working = false;
	
	def init() = {
		thread.setDaemon(true);
		thread.start();
	}
	
	private def run() = {
		while (keepAlive) {
			try {
				work = workManager.request();
				if (work != null) {
					working = true;
					work match {
						case wu:WorkUnit => wu.workManager = workManager;
						case _ =>
					}
					work match {
						case tu:TimeoutUnit => tu.started();
						case _ =>
					}
					work();
				} else {
					working = false;
					workManager.monitor.synchronized {
						workManager.monitor.wait(Math.round(workManager.threadWait * 1000));
					}
				}
			} catch {
				case exc:Throwable => {
					workManager.uncaughtExceptionHandler(exc);
				}
			}
			work match {
				case fu: FinishedUnit => fu.finished()
				case _ =>
			}
			work = null;
			
			if (workManager.threadYielding) {
				Thread.`yield`();
			}
		}
	}
	
	def update(time:Double) = {
		val work = this.work;
		work match {
			case tu:TimeoutUnit => {
				if (WorkManager.time - tu.getStarted > tu.timeout) {
					// TimeoutUnit has timed out
					thread.synchronized {
						thread.interrupt();
					}
				}
			}
			case _ =>
		}
	}
	
	def isWorking() = working;
	
	def hasWork() = work != null
}