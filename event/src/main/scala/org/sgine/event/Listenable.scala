package org.sgine.event

import actors.DaemonActor
import org.sgine.bus._
import org.sgine.ref.ReferenceType
import org.sgine.hierarchy.{Child, Parent}

/**
 * Listenable can be mixed in to provide the ability for event management on an object.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/2/11
 */
trait Listenable {
  protected[event] var listenersList: List[Listener] = Nil

  protected[event] val asynchronousActor = new DaemonActor {
    def act() {
      loop {
        react {
          case f: Function0[_] => f()
        }
      }
    }
  }.start()

  protected[event] def addListener(listener: Listener, referenceType: ReferenceType = ReferenceType.Hard) = synchronized {
    listenersList = (listener :: listenersList.reverse).reverse
    Bus.add(listener, referenceType)
    listener
  }

  protected[event] def removeListener(listener: Listener) = synchronized {
    listenersList = listenersList.filterNot(l => l == listener)
    Bus.remove(listener)
    listener
  }

  lazy val listeners = EventListenerBuilder(this)

  object filters {
    val target = TargetFilter(Listenable.this)

    def descendant(depth: Int = Int.MaxValue): Event => Boolean = {
      case event => event.target match {
        case _ if (depth == 0) => target(event)
        case child: Child if (Child.hasAncestor(child, Listenable.this, depth)) => true
        case _ => false
      }
    }

    def child() = descendant(1)

    def ancestor(depth: Int = Int.MaxValue): Event => Boolean = {
      case event => event.target match {
        case _ if (depth == 0) => target(event)
        case parent: Parent if (parent.hierarchy.hasDescendant(Listenable.this, depth)) => true
        case _ => false
      }
    }

    def parent() = ancestor(1)

    def thread(thread: Thread): Event => Boolean = {
      case event => event.thread == thread
    }
  }

  def fire(event: Event) = Event.fire(event, this)
}