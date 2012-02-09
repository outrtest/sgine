package org.sgine.event

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/3/11
 */
trait Event {
  def target: Listenable
}

object Event {
  def apply(target: Listenable) = BasicEvent(target)
}

case class BasicEvent(target: Listenable) extends Event