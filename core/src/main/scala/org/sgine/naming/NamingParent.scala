package org.sgine.naming

import org.sgine.reflect._
import collection.mutable.ArrayBuffer
import org.sgine.hierarchy.Named

/**
 * NamingParent can be mixed into a class to access fields from their names.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait NamingParent {
  implicit val namingParentInstance = this
  protected[naming] val fields = new ArrayBuffer[Named]()

  protected[sgine] def add(child: Named) = synchronized {
    if (!fields.contains(child)) {
      fields += child
    }
  }

  protected[naming] def name(child: NamedChild) = getClass.methods.find(m => if (m.args.isEmpty && m.returnType.`type`.name != "Unit") {
    m[Any](this) == child
  } else {
    false
  }).map(m => m.name).getOrElse(null)

  protected[naming] def notFound(name: String) = {
    throw new NullPointerException("Unable to find %s.%s".format(getClass.name, name))
  }
}