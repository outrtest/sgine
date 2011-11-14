package org.sgine

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait EnumeratedCombinable[E <: EnumEntry[E]] extends Enumerated[E] {
  def combine(enums: E*): E with Combined[E]
}

trait Combined[T] {
  def combined: List[T]

  override def equals(obj: Any) = !combined.find(e => e == obj).isEmpty

  override def toString = combined.mkString("[", ", ", "]")
}