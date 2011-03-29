package org.sgine

import util.Random

/**
 * Enumerated must be mixed into the companion object for Enum implementation.
 */
trait Enumerated[E <: Enum] extends Traversable[E] {
  private lazy val r = new Random()

  /**
   * Sequence of all associated Enums
   */
  def values: Seq[E]

  /**
   * Number of Enums
   */
  override def size = values.size

  /**
   * Retrieve Enum by index
   */
  def apply(index: Int) = values(index)

  /**
   * Next Enum from current.
   *
   * @param wrap Wraps around back to the beginning if true. Defaults to true.
   */
  def next(current: E, wrap: Boolean = true) = current.ordinal match {
    case index if (index == size - 1) => head
    case index => apply(index)
  }

  /**
   * Previous Enum from current.
   *
   * @param wrap Wraps around back to the end if true. Defaults to true.
   */
  def previous(current: E, wrap: Boolean = true) = current.ordinal match {
    case 0 => last
    case index => apply(index)
  }

  /**
   * Retrieves a random Enum.
   */
  def random = values(r.nextInt(size))

  def foreach[U](f: E => U) = values.foreach(f)

  def valueOf(s: String) = values.find(e => e.name == s)

  def indexOf(e: E) = values.indexOf(e)

  def indexOf(s: String): Int = valueOf(s) match {
    case Some(e) => indexOf(e)
    case None => -1
  }
}