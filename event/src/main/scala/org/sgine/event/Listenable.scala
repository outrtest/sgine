package org.sgine.event

import actors.DaemonActor
import org.sgine.bus._

/**
 * Listenable can be mixed in to provide the ability for event management on an object.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/2/11
 */
trait Listenable {
  protected[event] val asynchronousActor = new DaemonActor {
    def act() {
      loop {
        react {
          case (event: Event, listener: Listener) => listener.process(event)
        }
      }
    }
  }.start()

  val targetFilter = TargetFilter(this)

  object listeners {
    val synchronous = new Listeners(Listenable.this)
    val asynchronous = new Listeners(Listenable.this) {
      override protected def createListener(listener: Listener) = new AsynchronousListener(listener, targetFilter, Listenable.this)
    }
    val concurrent = new Listeners(Listenable.this) {
      override protected def createListener(listener: Listener) = new ConcurrentListener(listener, targetFilter)
    }
  }

  def fire(event: Event) = Bus(event)
}