package org.sgine.property

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ObjectPropertyParent(val parent: PropertyParent) extends PropertyParent {
  private val simpleName = getClass.getName.substring(getClass.getName.lastIndexOf('.') + 1)
  val name = simpleName.substring(0, simpleName.length - 1) match {
    case s => s.substring(s.lastIndexOf('$') + 1)
  }
}