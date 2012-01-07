package org.sgine.naming

/**
 * NamingFilter is used in conjunction with a NamingParent to represent a subset of fields within
 * the NamingParent of a specific type.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class NamingFilter[T](protected val parent: NamingParent)(implicit manifest: Manifest[T] = null)
  extends Seq[T] {
  private lazy val classType = if (manifest != null) {
    manifest.erasure
  }
  else {
    Class.forName(parent.getClass.getName.substring(0, parent.getClass.getName.length - 1))
  }
  protected lazy val fields = parent.fields
    .filter(m => classType.isAssignableFrom(m.returnType.`type`.javaClass)).toList

  /**
   * Finds the field by the provided name or throws a RuntimeException if not found.
   */
  def apply(name: String) = fields.find(m => m.name == name).getOrElse(parent.notFound(name)).invoke[T](parent)

  /**
   * The number of fields on this filter.
   */
  def length = fields.length

  /**
   * Retrieve a field value by index.
   */
  def apply(index: Int) = fields(index).invoke[T](parent)

  /**
   * Iterate over field values on this filter.
   */
  def iterator = fields.iterator.map(m => m.invoke[T](parent))
}