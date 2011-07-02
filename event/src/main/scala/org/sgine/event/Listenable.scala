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
  private lazy val actor = createActor()
  protected var map = Map.empty[Class[_], List[EventHandler[_]]]

  private def createActor() = {
    val a = new DaemonActor() {
      def act() {
        loop {
          react {
            case invocation: Function0[Unit] => invocation()
          }
        }
      }
    }
    a.start()
  }

  protected[event] def addHandler[T](handler: EventHandler[T]) = {
    synchronized {
      val list = map.get(handler.manifest.erasure) match {
        case Some(list) => handler :: list
        case None => List(handler)
      }
      map += handler.manifest.erasure -> list
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

  protected[event] def clear[T](clazz: Class[T]) = synchronized {
    map += clazz -> Nil
  }

  protected[event] def hasListeners(clazz: Class[_]) = map.get(clazz) match {
    case Some(list) if (!list.isEmpty) => true
    case _ => false
  }

  protected[event] def fire[T](clazz: Class[T], event: T) = {
    val listeners = map.getOrElse(clazz, Nil)
    invoke(event, listeners.asInstanceOf[List[EventHandler[T]]])
  }

  @tailrec
  private final def invoke[T](event: T, listeners: List[EventHandler[T]]): Unit = {
    if (!listeners.isEmpty) {
      val handler = listeners.head
      handler.processingMode match {
        case ProcessingMode.Synchronous => handler.invoke(event)
        case ProcessingMode.Asynchronous => actor ! (() => handler.invoke(event))
        case ProcessingMode.Concurrent => Executor.execute(new Runnable() {
          def run() = handler.invoke(event)
        })
      }

      invoke(event, listeners.tail)
    }
  }
}