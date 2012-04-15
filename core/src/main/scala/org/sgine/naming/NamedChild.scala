package org.sgine.naming

import org.sgine.hierarchy.Named


/**
 * NamedChild is used in conjunction with a NamingParent to determine the name of an underlying
 * field used within the NamingParent.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait NamedChild extends Named {
  protected def parent: NamingParent

  if (parent != null) {
    parent.add(this)
  }

  /**
   * The name of this object.
   */
  lazy val name = parent match {
    case null => getClass.getSimpleName.replaceAll("\\$", "")
    case p => p.name(this)
  }
}