package org.sgine.process.updatable

import scala.collection.mutable.SynchronizedQueue

/**
 * Executor extends Updatable to execute objects added to
 * the backing queue.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Executor[T] extends Updatable {
	private lazy val queue = new SynchronizedQueue[T]
	
	protected def add(value: T) = {
		queue.enqueue(value)
		ready = true
	}
	
	private def poll() = synchronized {
		queue.dequeueFirst(selectFirst)
	}
	
	private val selectFirst = (value: T) => true
	
	final protected def update(time: Double) = {
		executeNext()
	}
	
	@scala.annotation.tailrec
	private def executeNext(): Unit = {
		poll() match {
			case None =>
			case Some(value) => execute(value); executeNext()
		}
	}
	
	override protected def ready = queue.size > 0 || super.ready
	
	/**
	 * 
	 * 
	 * @param value
	 */
	protected def execute(value: T): Unit
}

trait PublicExecutor[T] extends Executor[T] {
	def +=(value: T) = add(value)
}