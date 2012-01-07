package org.sgine.naming

import org.sgine.reflect._

/**
 * NamingParent can be mixed into a class to access fields from their names.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait NamingParent {
  protected[naming] lazy val fields = getClass.methods.filter(accept)

  protected[naming] def value(name: String) = fields.find(m => m.name == name).getOrElse(notFound(name)).invoke[AnyRef](this)

  protected[sgine] def method(value: AnyRef) = fields.find(m => m.invoke[AnyRef](this) eq value).get

  protected def accept(method: EnhancedMethod) = method.args.isEmpty &&
    !method.name.contains("$") &&
    !method.isNative &&
    !"toString, hashCode, wait".contains(method.name)

  protected[naming] def notFound(name: String) = {
    throw new NullPointerException("Unable to find %s.%s".format(getClass.name, name))
  }
}