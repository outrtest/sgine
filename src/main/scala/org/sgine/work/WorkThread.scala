package org.sgine.work

import org.sgine.util._
import org.sgine.work.unit._

import scala.math._

private[work] class WorkThread (workManager:WorkManager) {
	private val thread = new Thread(FunctionRunnable(run))
	private var keepAlive = true
	
	private var work:() => Unit = _
	@volatile
	private var working = false
	
	def init() = {
		thread.setDaemon(true)
		thread.start()
	}
	
	private def run() = {
		while (keepAlive) {
			try {
				work = workManager.request()
				doWork
			} catch {
				case exc:Throwable => {
					workManager.uncaughtExceptionHandler(exc)
				}
			}
			work match {
				case fu: FinishedUnit => fu.finished()
				case _ =>
			}
			work = null
			
			if (workManager.threadYielding) {
				Thread.`yield`()
			}
		}
	}
	
	private def doWork(): Unit = {
		if (work != null) {
			working = true
			work match {
				case fu: FutureUnit[_] => {
					if (fu.isCancelled) {
						return
					}
				}
				case _ =>
			}
			work match {
				case wu:WorkUnit => {
					wu.workManager = workManager
					wu.workThread = this
				}
				case _ =>
			}
			work match {
				case tu:TimeoutUnit => tu.started()
				case _ =>
			}
			work()
		} else {
			working = false
			workManager.monitor.synchronized {
				workManager.monitor.wait(round(workManager.threadWait * 1000))
			}
		}
	}
	
	def update(time:Double) = {
		val work = this.work
		work match {
			case tu:TimeoutUnit => {
				if (WorkManager.time - tu.getStarted > tu.timeout) {
					// TimeoutUnit has timed out
					thread.synchronized {
						thread.interrupt()
					}
				}
			}
			case _ =>
		}
	}
	
	def isWorking() = working
	
	def hasWork() = work != null
	
	def interrupt() = thread.interrupt()
}