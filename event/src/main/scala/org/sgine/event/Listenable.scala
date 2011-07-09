package org.sgine.event

import annotation.tailrec
import org.sgine.{Priority, ProcessingMode}
import org.sgine.concurrent.{WorkQueue, Time, Concurrent}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 6/21/11
 */
trait Listenable extends Concurrent {
  def parent: Listenable = null

  /**
   * Provides the ability to add listeners directly to this Listenable instance. It is generally preferable to use the
   * EventSupport mechanism instead for better type-safety and validation.
   */
  lazy val listeners = new Listeners(this)

  protected var map = Map.empty[Class[_], List[EventHandler[_]]]

  protected[event] def addHandler[T](handler: EventHandler[T]) = {
    synchronized {
      val list: List[EventHandler[T]] = map.get(handler.manifest.erasure) match {
        case Some(list) => if (list.contains(handler)) {
          list.asInstanceOf[List[EventHandler[T]]]
        } else {
          (handler :: list).asInstanceOf[List[EventHandler[T]]]
        }
        case None => List(handler)
      }
      map += handler.manifest.erasure -> list.sortWith(sorter)
    }
  }

  private val sorter = (eh1: EventHandler[_], eh2: EventHandler[_]) => eh1.priority > eh2.priority

  protected[event] def removeHandler[T](handler: EventHandler[T]) = {
    synchronized {
      map.get(handler.manifest.erasure) match {
        case Some(list) => {
          map += handler.manifest.erasure -> list.filterNot(eh => eh == handler)
          true
        }
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
          case ProcessingMode.Synchronous => handler.invoke(event, this)
          case ProcessingMode.Asynchronous => asynchronous(handler.invoke(event, this))
          case ProcessingMode.Concurrent => concurrent(handler.invoke(event, Listenable.this))
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

class Listeners(listenable: Listenable) {
  def +=[T](listener: T => Any)(implicit manifest: Manifest[T]): EventHandler[T] = {
    this += new EventHandler(listener, ProcessingMode.Synchronous)(manifest)
  }

  def +=[T](handler: EventHandler[T]): EventHandler[T] = {
    listenable.addHandler(handler)
    handler
  }

  def -=[T](handler: EventHandler[T]): EventHandler[T] = {
    listenable.removeHandler(handler)
    handler
  }

  def size[T](implicit manifest: Manifest[T]): Int = listenable.size(manifest.erasure)

  def clear[T]()(implicit manifest: Manifest[T]): Unit = listenable.clear(manifest.erasure)

  def synchronous[T](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Synchronous)(manifest)
    this += handler
    handler
  }

  def asynchronous[T](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Asynchronous)(manifest)
    this += handler
    handler
  }

  def concurrent[T](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Concurrent)(manifest)
    this += handler
    handler
  }

  def listen[T](processingMode: ProcessingMode = ProcessingMode.Synchronous,
                priority: Double = Priority.Normal,
                recursion: Recursion = Recursion.Current,
                workQueue: WorkQueue = null)
               (f: PartialFunction[T, Any])
               (implicit manifest: Manifest[T]): EventHandler[T] = {
    val handler = new EventHandler[T](f, processingMode, priority, recursion, workQueue)(manifest)
    this += handler
    handler
  }

  def hasListeners[T](implicit manifest: Manifest[T]): Boolean = listenable.hasListeners(manifest.erasure)

  def waitFor[T](time: Double)(implicit manifest: Manifest[T]) = {
    var response: Option[T] = None
    val handler = new EventHandler((t: T) => response = Some(t), ProcessingMode.Synchronous)(manifest)
    this += handler
    Time.waitFor(time) {
      response != None
    }
    this -= handler
    response
  }
}