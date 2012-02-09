package org.sgine.event

import org.sgine.ref.ReferenceType
import annotation.tailrec
import actors.DaemonActor
import org.sgine.bus._
import org.sgine.{Parent, Child, Priority}
import collection.mutable.ListBuffer

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
          case event: Event => processOn(event, listeners.asynchronous.listeners, null)
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
      val routing = processOn(event, listeners.synchronous.listeners, null)
      if (routing.continuing) {
        if (listeners.asynchronous.listeners.nonEmpty) {
          asynchronousActor ! event
        }
        val buffer = routing match {
          case results: RoutingResults => ListBuffer[Any](results.results: _*)
          case _ => null
        }
        processOn(event, listeners.concurrent.listeners, buffer)
      } else {
        routing
      }
    }
  }

  @tailrec
  private def processOn(message: Event, listeners: List[Listener], buffer: ListBuffer[Any]): Routing = {
    if (listeners.nonEmpty) {
      val listener = listeners.head
      listener.process(message) match {
        case Routing.Stop if (buffer == null) => Routing.Stop
        case Routing.Stop => Routing.Results(buffer.toList)
        case response: RoutingResponse => response
        case results: RoutingResults => {
          val b = buffer match {
            case null => new ListBuffer[Any]()
            case _ => buffer
          }
          b ++= results.results
          processOn(message, listeners.tail, b)
        }
        case _ => processOn(message, listeners.tail, buffer)
      }
    } else if (buffer != null) {
      Routing.Results(buffer.toList)
    } else {
      Routing.Continue
    }
  }

  object listeners {
    val synchronous = new Listeners(Listenable.this)
    val asynchronous = new Listeners(Listenable.this)
    val concurrent = new Listeners(Listenable.this) {
      override protected def createListener(listener: Listener) = new ConcurrentListener(listener)
    }

    /**
     * Fired when an Event is received on an ancestor of this object (ex. parent, grandparent, etc.)
     */
    def ancestor[T <: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
      val listener = new FunctionalListener[T](Listener.withFallthrough(f))
      val node = new AncestorNode(Listenable.this, listener)
      Bus.add(node)
      node
    }

    /**
     * Fired when an Event is received on a descendant of this object (ex. child, grandchild, etc.)
     */
    def descendant[T <: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
      val listener = new FunctionalListener[T](Listener.withFallthrough(f))
      val node = new DescendantNode(Listenable.this, listener)
      Bus.add(node)
      node
    }

    /**
     * Fired when an Event is received on a parent of this object
     */
    def parent[T <: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
      val listener = new FunctionalListener[T](Listener.withFallthrough(f))
      val node = new AncestorNode(Listenable.this, listener, 1)
      Bus.add(node)
      node
    }

    /**
     * Fired when an Event is received on a child of this object
     */
    def child[T <: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
      val listener = new FunctionalListener[T](Listener.withFallthrough(f))
      val node = new DescendantNode(Listenable.this, listener, 1)
      Bus.add(node)
      node
    }
  }

  def fire(event: Event) = Bus(event)

  Bus.add(node, ReferenceType.Weak)
}

class AncestorNode(descendant: Any, listener: Listener, maxDepth: Int = Int.MaxValue, val priority: Priority = Priority.Normal) extends Node {
  def receive(message: Any) = {
    val response = message match {
      case event: Event => event.target match {
        case parent: Parent[_] if (parent.hasDescendant(descendant, maxDepth)) => listener.process(event)
        case _ => // Not a Child instance
      }
      case _ => // Not an Event
    }
    response match {
      case routing: Routing => routing
      case _ => Routing.Continue
    }
  }
}

class DescendantNode(ancestor: Any, listener: Listener, maxDepth: Int = Int.MaxValue, val priority: Priority = Priority.Normal) extends Node {
  def receive(message: Any) = {
    val response = message match {
      case event: Event => event.target match {
        case child: Child[_] if (child.hasAncestor(ancestor, maxDepth)) => listener.process(event)
        case _ => // Not a Child instance
      }
      case _ => // Not an Event
    }
    response match {
      case routing: Routing => routing
      case _ => Routing.Continue
    }
  }
}