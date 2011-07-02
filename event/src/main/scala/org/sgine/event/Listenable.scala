package org.sgine.event

import annotation.tailrec
import org.sgine.ProcessingMode
import actors.DaemonActor
import org.sgine.concurrent.Executor
/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 6/21/11
 */
trait Listenable {
  def parent: Listenable = null

  private lazy val actor = createActor()
  protected var map = Map.empty[Class[_], List[EventHandler[_]]]

  private def createActor() = {
    val a = new DaemonActor() {
      def act() {
        loop {
          react {
            case invocation: Function0[_] => invocation()
          }
        }
      }
    }
    a.start()
  }

  protected[event] def addHandler[T](handler: EventHandler[T]) = {
    synchronized {
      val list: List[EventHandler[T]] = map.get(handler.manifest.erasure) match {
        case Some(list) => (handler :: list).asInstanceOf[List[EventHandler[T]]]
        case None => List(handler)
      }
      map += handler.manifest.erasure -> list.sorted
    }
  }

  protected[event] def removeHandler[T](handler: EventHandler[T]) = {
    synchronized {
      map.get(handler.manifest.erasure) match {
        case Some(list) => map += handler.manifest.erasure -> list.filter(eh => eh == handler); true
        case None => false
      }
    }
  }

  protected[event] def size[T](clazz: Class[T]) = map.get(clazz) match {
    case Some(list) => list.size
    case None => 0
  }

  protected[event] def clear[T](clazz: Class[T]) = synchronized {
    map += clazz -> Nil
  }

  protected[event] def hasListeners(clazz: Class[_]) = map.get(clazz) match {
    case Some(list) if (!list.isEmpty) => true
    case _ => false
  }

  protected[event] def shouldFire(clazz: Class[_]) = hasListeners(clazz)

  protected[event] def fire[T](clazz: Class[T], event: T) = {
    event match {
      case event: Event => event._target = this
      case _ =>
    }
    fireRecursive[T](clazz, event, Recursion.Current)

    // Fire event on the Bus
    val cancelled = event match {
      case event: Event => event.cancelled
      case _ => false
    }
    if (!cancelled) {
      val listeners = Bus.map.getOrElse(clazz, Nil).asInstanceOf[List[EventHandler[T]]]
      Bus.invoke(event, listeners, Recursion.Current)
    }
  }

  private def fireRecursive[T](clazz: Class[T], event: T, recursion: Recursion): Unit = {
    val listeners = map.getOrElse(clazz, Nil).asInstanceOf[List[EventHandler[T]]]
    event match {
      case event: Event => event._currentTarget = this
      case _ =>
    }
    if (!invoke(event, listeners, recursion)) {
      if (parent != null) {
        parent.fireRecursive(clazz, event, Recursion.Children)
      }
    }
  }

  @tailrec
  private[event] final def invoke[T](event: T, listeners: List[EventHandler[T]], recursion: Recursion): Boolean = {
    if (!listeners.isEmpty) {
      val handler = listeners.head
      if (recursion == Recursion.Current || recursion == handler.recursion) {
        handler.processingMode match {
          case ProcessingMode.Synchronous => handler.invoke(event)
          case ProcessingMode.Asynchronous => actor ! (() => handler.invoke(event))
          case ProcessingMode.Concurrent => Executor.execute(new Runnable() {
            def run() = handler.invoke(event)
          })
        }
      }

      val cancelled = event match {
        case event: Event => event.cancelled
        case _ => false
      }
      if (cancelled) {
        true
      } else {
        invoke(event, listeners.tail, recursion)
      }
    } else {
      false
    }
  }
}