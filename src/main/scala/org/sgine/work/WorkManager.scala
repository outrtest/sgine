package org.sgine.work

import org.sgine.util._
import org.sgine.work.unit._

import java.util.concurrent._
import java.util.concurrent.locks._

class WorkManager (val name: String) {
	/**
	 * @see WorkManager#DEFAULT_THREAD_SETTLING
	 */
	var threadSettling = WorkManager.DEFAULT_THREAD_SETTLING
	/**
	 * @see WorkManager#DEFAULT_WORK_LOAD_THRESHOLD
	 */
	var workLoadThreshold = WorkManager.DEFAULT_WORK_LOAD_THRESHOLD
	/**
	 * @see WorkManager#DEFAULT_WORK_DELAY_THRESHOLD
	 */
	var workDelayThreshold = WorkManager.DEFAULT_WORK_DELAY_THRESHOLD
	/**
	 * @see WorkManager#DEFAULT_THREAD_WAIT
	 */
	var threadWait = WorkManager.DEFAULT_THREAD_WAIT
	/**
	 * @see WorkManager#DEFAULT_MAX_THREADS
	 */
	var maxThreads = WorkManager.DEFAULT_MAX_THREADS
	/**
	 * @see WorkManager#DEFAULT_MONITORING_DELAY
	 */
	var monitoringDelay = WorkManager.DEFAULT_MONITORING_DELAY
	/**
	 * @see WorkManager#DEFAULT_ALLOW_OVERLOADING
	 */
	var allowOverloading = WorkManager.DEFAULT_ALLOW_OVERLOADING
	/**
	 * @see WorkManager#DEFAULT_THREAD_YIELDING
	 */
	var threadYielding = WorkManager.DEFAULT_THREAD_YIELDING
	/**
	 * processes any uncaught exceptions thrown through work units
	 */
	var uncaughtExceptionHandler = (exc:Throwable) => {
		exc.printStackTrace()
	}
	/**
	 * @see WorkManager#DEFAULT_MAX_QUEUE
	 */
	var maxQueue = WorkManager.DEFAULT_MAX_QUEUE
	
	private lazy val queue = new ArrayBlockingQueue[() => Unit](maxQueue)
	private var workers: List[WorkThread] = Nil
	
	private[work] val monitor = new Thread(FunctionRunnable(run))
	
	private var started = false
	private var keepAlive = true
	
	private def init() = {
		if (!started) {
			synchronized {
				if (!started) {
					started = true
					monitor.setDaemon(true)
					monitor.start()
					
					addWorker()
				}
			}
		}
	}
	
	private var time: Double = 0.0
	private var allProcessing = true
	
	private val updateWorkThread = (w: WorkThread) => {
		w.update(time)
		if (!w.isWorking) {
			allProcessing = false
		}
	}
	
	private def run():Unit = {
		while (keepAlive) {
			val lastTime = WorkManager.time
			WorkManager._time = System.currentTimeMillis
			
			time = (WorkManager.time - lastTime) / 1000.0
			val threadCount = workers.size
			val workCount = queue.size
			allProcessing = true
			
			// Update WorkThreads
			workers.foreach(updateWorkThread)

			// Check to see if overloaded with work
			if (threadCount < maxThreads) {
				val nextWork = queue.peek()
				nextWork match {
					case wu:WorkUnit => {
						if (WorkManager.time - wu.getEnqueued > workDelayThreshold) {
							addWorker()
						}
					}
					case _ =>
				}
				if (allProcessing) {						// All threads are processing
					if (workers.length < maxThreads) {		// We haven't reached maximum thread count
						addWorker()
					} else if ((workCount > workLoadThreshold) && (allowOverloading)) {		// Too much work and cap has been reached, but we allow overloading
						addWorker()
					}
				}
//				println("Test: " + allProcessing + ", " + workCount + ", " + workLoadThreshold + ", " + allowOverloading + ", " + workers.length)
//				if ((allProcessing) && (workCount > workLoadThreshold) && (allowOverloading)) {
//					addWorker()
//				}
			}
			
			Thread.sleep(10)
		}
	}
	
	private def addWorker() = {
		val w = new WorkThread(this)
//		workers.add(w)
		synchronized {
			workers = w :: workers
		}
		w.init()
	}
	
	/**
	 * Processes the "values" through "f" using WorkManager threads
	 * in a fully asynchronous way. If "blocking" is true the method
	 * will block until all processing has successfully completed.
	 * 
	 * @param values
	 * @param f
	 */
	def process[A](values: Seq[A], f: (A) => Unit, blocking: Boolean = true) = {
		val count = new java.util.concurrent.atomic.AtomicInteger(values.length)
		for (v <- values) {
			val work: () => Unit = () => {
				try {
					f(v)
				} finally {
					count.addAndGet(-1)
				}
			}
			this += work
		}
		if (blocking) {
			while (count.get > 0) {
				Thread.sleep(10)
			}
		}
	}
	
	def +=[T <: () => Unit](work: T): T = {
		init()
		
		queue.put(work)
		
		work
	}
	
	def -=(work:() => Unit) = {
		queue.remove(work)
	}
	
	def request():() => Unit = {
		// TODO: migrate toward take and put for better blocking
		var w = queue.poll()
		var first:Function0[Unit] = null
		
		do {
			if ((first == w) && (first != null)) {
				queue.add(w)
				return null
			}
			w match {
				case du:DependentUnit => if (du.isReady()) return du
				case f:Function0[_] => return f
				case _ =>
			}
			if (w != null) queue.put(w)
			if (first == null) first = w
			w = queue.poll()
		} while(w != null)
		
		return null
	}
	
	def threadCount = workers.length
	
	def waitForIdle(timeout:Long, unit:TimeUnit):Boolean = {
		val t = System.currentTimeMillis + TimeUnit.MILLISECONDS.convert(timeout, unit)
		var b = false
		while (System.currentTimeMillis < t) {
			if (b) {
				if (isIdle()) {
					return true
				}
				b = false
			} else if (isIdle()) {
				b = true
			}
			Thread.sleep(10)
		}
		isIdle()
	}
	
	def isIdle():Boolean = {
		if (hasWork() == 0) {
			if (queue.size == 0) {
				if (hasWork() == 0) {
					return queue.size == 0
				}
			}
		}
		false
	}
	
	def working():Int = {
		var count = 0
		for (w <- workers) {
			if (w.isWorking) {
				count += 1
			}
		}
		count
	}
	
	def hasWork():Int = {
		var count = 0
		for (w <- workers) {
			if (w.hasWork) {
				count += 1
			}
		}
		count
	}
	
	def workQueue = queue.size
}

object WorkManager {
	/**
	 * The default amount of time (in seconds) to delay after exceeding
	 * the maximum thread count before settling the count back down.
	 * 
	 * Default value: 5 minutes
	 */
	val DEFAULT_THREAD_SETTLING = 5.0 * 60.0
	/**
	 * The threshold that must be achieved or exceeded in the work units
	 * waiting in the queue before another thread is added. This will only
	 * apply to the maxThreads.
	 * 
	 * Default value: 10
	 */
	val DEFAULT_WORK_LOAD_THRESHOLD = 10
	/**
	 * The time (in seconds) threshold that must be achieved or exceeded on
	 * an item in the normal priority queue for an additional worker thread to be
	 * automatically added. This will only apply up to the maxThreads.
	 * 
	 * Default value: 100 milliseconds
	 */
	val DEFAULT_WORK_DELAY_THRESHOLD = 0.1
	/**
	 * The amount of time for each ProcessingThread to wait before recycling.
	 * 
	 * Default value: 50 milliseconds
	 */
	val DEFAULT_THREAD_WAIT = 0.05
	/**
	 * The default starting maximum thread count
	 * 
	 * Default value: 500
	 */
	val DEFAULT_MAX_THREADS = 500
	/**
	 * The amount of time between iterations of the monitoring thread. This
	 * is what checks to see if a work unit that has a timeout period has been
	 * exceeded and should be terminated. Also, this determines the delay between
	 * checks if the max thread count should be increased.
	 * 
	 * Default value: 100 milliseconds
	 */
	val DEFAULT_MONITORING_DELAY = 0.1
	/**
	 * If true the thread pool will be allowed to exceed the max thread count temporarily
	 * if the work load is too high. Thread settling value is used to determine when to
	 * release overloading threads once the work-load comes back down to a reasonable amount.
	 * 
	 * Default value: true
	 */
	val DEFAULT_ALLOW_OVERLOADING = true
	/**
	 * If true the worker threads will yield upon completion of work before looking for
	 * additional work to do. This can keep the CPU load down when there is a steady stream
	 * of work without an end in sight.
	 * 
	 * Default value: true.
	 */
	val DEFAULT_THREAD_YIELDING = true
	/**
	 * The default maximum queue size for new WorkManagers. If the number of items in the queue
	 * is full callers will block until space is available for additional elements to be added.
	 * 
	 * Default value: 100000
	 */
	val DEFAULT_MAX_QUEUE = 100000
	
	private var _time = System.currentTimeMillis()
	
	def time = _time
}