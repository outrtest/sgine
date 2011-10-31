package org.sgine.naming

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class NamingFilter[T](parent: NamingParent)(implicit manifest: Manifest[T] = null) extends Seq[T] {
  private lazy val classType = if (manifest != null) {
    manifest.erasure
  } else {
    Class.forName(parent.getClass.getName.substring(0, parent.getClass.getName.length - 1))
  }
  private lazy val fields = parent.fields.filter(m => classType.isAssignableFrom(m.returnType.`type`.javaClass)).toList

  def apply(name: String) = fields.find(m => m.name == name).getOrElse(parent.notFound(name)).apply[T](parent)

  def length = fields.length

  def apply(index: Int) = fields(index).apply[T](parent)

  def iterator = fields.iterator.map(m => m[T](parent))
}