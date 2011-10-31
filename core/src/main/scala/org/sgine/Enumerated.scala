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

  val values = new NamingFilter[E](this)

  def random = values(r.nextInt(values.length))

  override protected def accept(method: EnhancedMethod) = if (method.name == "random") false else super.accept(method)
}