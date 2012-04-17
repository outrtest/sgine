package org.sgine.naming

import org.sgine.hierarchy.Named
import util.Random

/**
 * NamingFilter is used in conjunction with a NamingParent to represent a subset of fields within
 * the NamingParent of a specific type.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class NamingFilter[T <: Named](protected val parent: NamingParent)(implicit manifest: Manifest[T] = null) extends Seq[T] {
  private lazy val r = new Random()

  private lazy val classType = if (manifest != null) {
    manifest.erasure
  } else {
    Class.forName(parent.getClass.getName.substring(0, parent.getClass.getName.length - 1))
  }
//  protected lazy val fields = parent.fields.filter(v => classType.isAssignableFrom(v.getClass)).toList
  protected lazy val fields = parent.fields.collect {
    case v if (classType.isAssignableFrom(v.getClass)) => v.asInstanceOf[T]
  }.toList

  /**
   * Finds the field by the provided name or throws a RuntimeException if not found.
   */
  def apply(name: String) = fields.find(m => m.name == name).getOrElse(parent.notFound(name))

  /**
   * The number of fields on this filter.
   */
  def length = fields.length

  /**
   * Retrieve a field value by index.
   */
  def apply(index: Int) = fields(index)

  /**
   * Iterate over field values on this filter.
   */
  def iterator = fields.iterator

  /**
   * Retrieves a random enum.
   */
  def random = apply(r.nextInt(length))
}