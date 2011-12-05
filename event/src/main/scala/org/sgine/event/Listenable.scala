package org.sgine.event

import org.sgine.ref.ReferenceType
import annotation.tailrec
import actors.DaemonActor
import org.sgine.bus._
import org.sgine.{Parent, Child, Priority}

/**
 * Listenable can be mixed in to provide the ability for event management on an object.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/2/11
 */
trait Listenable {
  protected def priority = Priority.Normal

  protected lazy val asynchronousActor = new DaemonActor {
    def act() {
      loop {
        react {
          case event: Event => processOn(event, listeners.asynchronous.listeners)
        }
      }
    }
  }.start()

  private val node = new TypedNode[Event] with FilteredNode {
    def manifest = Manifest.classType[Event](classOf[Event])

    def priority = Listenable.this.priority

    protected def filter(message: Any) = message match {
      case event: Event if (event.target == Listenable.this) => false
      case _ => true
    }

    protected def process(event: Event) = {
      processOn(event, listeners.synchronous.listeners)
      if (listeners.asynchronous.listeners.nonEmpty) {
        asynchronousActor ! event
      }
      processOn(event, listeners.concurrent.listeners)
      Routing.Continue
    }
  }

  @tailrec
  private def processOn(message: Event, listeners: List[Listener]): Unit = {
    if (listeners.nonEmpty) {
      val listener = listeners.head
      listener.process(message)
      processOn(message, listeners.tail)
    }
  }

  object listeners {
    val synchronous = new Listeners(Listenable.this)
    val asynchronous = new Listeners(Listenable.this)
    val concurrent = new Listeners(Listenable.this) {
      override protected def createListener(listener: Listener) = new ConcurrentListener(listener)
    }

    def ancestor[T >: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
      val listener = new FunctionalListener[T](Listener.withFallthrough(f))
      val node = new AncestorNode(Listenable.this, listener)
      Bus.add(node)
      node
    }

    def descendant[T >: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
      val listener = new FunctionalListener[T](Listener.withFallthrough(f))
      val node = new DescendantNode(Listenable.this, listener)
      Bus.add(node)
      node
    }
  }

  def fire(event: Event) = Bus(event)

  Bus.add(node, ReferenceType.Weak)
}

class AncestorNode(descendant: Any, listener: Listener, val priority: Priority = Priority.Normal) extends Node {
  def receive(message: Any) = {
    message match {
      case event: Event => event.target match {
        case parent: Parent[_] if (parent.hasDescendant(descendant)) => listener.process(event)
        case _ => // Not a Child instance
      }
      case _ => // Not an Event
    }
    Routing.Continue
  }
}

class DescendantNode(ancestor: Any, listener: Listener, val priority: Priority = Priority.Normal) extends Node {
  def receive(message: Any) = {
    message match {
      case event: Event => event.target match {
        case child: Child[_] if (child.hasAncestor(ancestor)) => listener.process(event)
        case _ => // Not a Child instance
      }
      case _ => // Not an Event
    }
    Routing.Continue
  }
}