package org.sgine.property

import org.sgine.hierarchy.Named._

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ObjectPropertyParent(val parent: PropertyParent) extends PropertyParent {
  val name = simpleName(getClass).substring(0, simpleName(getClass).length - 1) match {
    case s => s.substring(s.lastIndexOf('$') + 1)
  }
}