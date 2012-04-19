package org.sgine.property

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ObjectPropertyParent(val parent: PropertyParent) extends PropertyParent {
  val name = getClass.getSimpleName.substring(0, getClass.getSimpleName.length - 1) match {
    case s => s.substring(s.lastIndexOf('$') + 1)
  }
}