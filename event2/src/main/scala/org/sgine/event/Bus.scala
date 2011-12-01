package org.sgine.event

/**
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Bus extends Listenable[Any] {
  def apply[E]() = this.asInstanceOf[Listenable[E]]
}