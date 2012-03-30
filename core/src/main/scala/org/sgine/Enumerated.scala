package org.sgine

import naming.{NamingFilter, NamingParent}
import reflect.EnhancedMethod
import util.Random

/**
 * Enumerated represents the companion object for EnumEntry instances.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Enumerated[E <: EnumEntry[E]] extends NamingParent {
  private lazy val r = new Random()

  /**
   * The name of this Enumerated.
   */
  lazy val name = getClass.getSimpleName.replaceAll("\\$", "")

  /**
   * Retrieve an enum by index.
   */
  def apply(index: Int) = values(index)

  /**
   * Retrieve an enum by name.
   */
  def apply(name: String) = values.find(e => e.name == name)

  /**
   * Collection of all the enums associated with this Enumerated.
   */
  object values extends NamingFilter[E](this) {
    override def apply(index: Int) = {
      fields.map(m => m.invoke[E](parent)).find(v => v.ordinal == index)
        .getOrElse(throw new IndexOutOfBoundsException("No entry at ordinal %s".format(index)))
    }
  }

  /**
   * Retrieves a random enum.
   */
  def random = values(r.nextInt(values.length))

  override protected def accept(method: EnhancedMethod) = if (method.name == "random") {
    false
  } else {
    super.accept(method)
  }
}