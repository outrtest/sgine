package org.sgine.event

/**
 * Event is the core object sent to listeners.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/3/11
 */
trait Event {
  def target: Listenable

  val thread = Thread.currentThread()
}

object Event {
  def apply(target: Listenable) = BasicEvent(target)
}

case class BasicEvent(target: Listenable) extends Event