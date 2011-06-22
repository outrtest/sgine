package org.sgine.event

import annotation.tailrec
import actors.DaemonActor
import org.sgine.concurrent.Executor

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 6/21/11
 */
trait Listenable {
  private var map = Map.empty[Class[_], List[EventHandler[_]]]
  private lazy val actor = createActor()

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

  protected[event] def hasListeners(clazz: Class[_]) = map.contains(clazz)

  protected[event] def fireSynchronous[T](clazz: Class[T], event: T) = {
    val listeners = map.getOrElse(clazz, Nil)
    invoke(event, listeners.asInstanceOf[List[EventHandler[T]]])
  }

  protected[event] def fireAsynchronous[T](clazz: Class[T], event: T) = {
    val f = () => fireSynchronous(clazz, event)
    actor ! f
  }

  protected[event] def fireConcurrent[T](clazz: Class[T], event: T) = {
    Executor.execute(new Runnable() {
      def run() = fireSynchronous(clazz, event)
    })
  }

  @tailrec
  protected[event] final def invoke[T](event: T, listeners: List[EventHandler[T]]): Unit = {
    if (!listeners.isEmpty) {
      val handler = listeners.head
      handler.invoke(event)

      invoke(event, listeners.tail)
    }
  }
}