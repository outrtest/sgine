package org.sgine.process.updatable

import java.lang.ref.WeakReference

import java.util.concurrent.atomic.AtomicBoolean

import org.sgine.core._

import org.sgine.process._

import org.sgine.util.FunctionRunnable

import scala.math._

trait Updatable {
	@volatile private var elapsed = 0.0
	@volatile private var _updating = false
	private lazy val readyState = createReadyState()
	
	/**
	 * Whether this Updatable is currently being updated.
	 * 
	 * @return
	 * 		updating state boolean
	 */
	protected def updating = _updating
	
	/**
	 * The last time this Updatable was invoked. Distance
	 * is defined in seconds.
	 * 
	 * @return
	 * 		time in seconds since last update
	 */
	protected def lastUpdated = elapsed
	
	/**
	 * Invoked when this Updatable is updated. This method
	 * must define the logic that should execute upon
	 * update.
	 * 
	 * @param time
	 * 		distance in seconds since last update
	 */
	protected def update(time: Double): Unit
	
	/**
	 * Invoked after an update occurs. This allows traits to
	 * inject functionality after an update is finished by
	 * doing an abstract override.
	 */
	protected def updated() = {
	}
	
	// Called by companion when updating
	private val invokeUpdate = () => {
		readyState.set(false)
		try {
			update(0.0)		// TODO: time
			updated()
		} finally {
			elapsed = 0.0
			_updating = false
		}
		
		Updatable.synchronized {
			Updatable.changed.set(true)		// Set temporal changed state
			Updatable.notifyAll()			// Notify the companion
		}
	}
	
	/**
	 * Sets the ready state of this Updatable. This method
	 * should be invoked internally when this Updatable is
	 * ready to be updated.
	 */
	protected def ready_=(value: Boolean) = {
		readyState.set(value)	// Modify state
		if (value) {
			Updatable.synchronized {
				Updatable.changed.set(true)		// Set temporal changed state
				Updatable.notifyAll()			// Notify the companion
			}
		}
	}
	
	protected def ready = readyState.get
	
	/**
	 * Retrieves the ready state of this Updatable. Under normal
	 * circumstances this simply refers to the internal ready state
	 * as set by ready().
	 * 
	 * @return
	 * 		true if ready to update
	 */
	protected def isReady() = readyState.get
	
	/**
	 * The estimated amount of time in seconds until the next
	 * invocation of this Updatable. This defaults to MaxValue
	 * of Double. If this Updatable notifies upon ready then
	 * this is not necessary to be utilized. Only time-based
	 * instances or indirect updatables need override this
	 * method.
	 * 
	 * @return
	 * 		time in seconds to next ready
	 */
	protected def estimatedReady() = Double.MaxValue
	
	/**
	 * Explicitly removes this Updatable from any future updating.
	 */
	protected def die() = Updatable.remove(this)
	
	// Invoked on first ready
	private def createReadyState() = {
		Updatable.add(this)		// Add this Updatable
		
		new AtomicBoolean()		// Return new atomic boolean
	}
}

object Updatable {
	@volatile var precision: Precision = Precision.Default
	@volatile var maxWait = 10.0
	
	private val thread = createThread()
	// TODO: support hard references
	private var updatables: List[WeakReference[Updatable]] = Nil
	private val changed = new java.util.concurrent.atomic.AtomicBoolean(false)
	
	// Add the Updatable to the list as weak reference
	private def add(updatable: Updatable) = synchronized {
		updatables = new WeakReference(updatable) :: updatables
		changed.set(true)
		notifyAll()
	}
	
	private def remove(updatable: Updatable) = synchronized {
		updatables = updatables.filterNot(_.get == updatable)
	}
	
	// Creates the internal thread to monitor updatables
	private def createThread() = {
		val t = new Thread(FunctionRunnable(run))
		t.setDaemon(true)
		t.start()
		t
	}
	
	private def run() = {
		var precision = this.precision
		var last = precision.time
		while (true) {		// Don't worry, it's a daemon thread
			val current = precision.time
			val time = (current - last) / precision.conversion
			var waitTime = maxWait
			for (ref <- updatables) {
				ref.get match {
					case null => removeRef(ref)		// Lost reference
					case u => {
						u.elapsed += time		// Update elapsed time
						u match {
							// Already updating
							case u if (u.updating) =>
							// Update in another thread
							case u if (u.isReady) => {
								u._updating = true
								Process(u.invokeUpdate, ProcessHandling.Enqueue)
							}
							// Determine next availability
							case u => waitTime = min(waitTime, u.estimatedReady)
						}
					}
				}
			}
			
			// Wait
			val delay = round(waitTime * 1000.0)
			if (delay > 0) {
				synchronized {
					if (!changed.compareAndSet(true, false)) {
						wait(delay)
					}
				}
			}
			
			// Update local precision
			precision = this.precision
		}
	}
	
	private def removeRef(ref: WeakReference[Updatable]) = synchronized {
		updatables = updatables.filterNot(_ == ref)
	}
}