package org.sgine

import collection.mutable.ArrayBuffer
import util.Random

/**
 * Enumerated represents the companion object for EnumEntry instances.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Enumerated[E <: EnumEntry[E]] {
  implicit val instance = this
  private val array = new ArrayBuffer[E]()
  private lazy val r = new Random()

  /**
   * The name of this Enumerated.
   */
  lazy val name = getClass.getSimpleName.replaceAll("\\$", "")

  /**
   * Retrieve the EnumEntry by name.
   *
   * @param name the name of the EnumEntry as defined by the field.
   * @return EnumEntry or null if not found
   */
  def apply(name: String) = array.find(e => e.name == name).getOrElse(null)

  /**
   * Retrieve the EnumEntry by index.
   *
   * @param index of the EnumEntry.
   * @return EnumEntry or IndexOutOfBoundsException
   */
  def apply(index: Int) = array(index)

  /**
   * All EnumEntries for this Enumerated instance.
   */
  def values: Seq[E] = array

  /**
   * Retrieves a random enum.
   */
  def random = values(r.nextInt(values.length))

  protected[sgine] def add(e: E) = synchronized {
    array += e
  }

  protected[sgine] def name(e: E) = getClass.getDeclaredFields.find(f => if (classOf[EnumEntry[_]].isAssignableFrom(f.getType)) {
    f.setAccessible(true)
    f.get(this) == e
  } else {
    false
  }).map(f => f.getName).getOrElse(throw new NullPointerException("Unable to find name for %s".format(e.getClass)))

  protected[sgine] def index(e: E) = array.indexOf(e)
}