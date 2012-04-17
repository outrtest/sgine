package org.sgine.property

import org.sgine.naming.{NamingFilter, NamingParent}
import org.sgine.hierarchy.{Named, Child}


/**
 * PropertyParent leverages NamingParent to define a "properties" value listing all properties
 * contained within this class.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait PropertyParent extends NamingParent with Child with Named {
  def parent: PropertyParent

  implicit val childrenParent = this

  /**
   * Access all the properties associated with this class.
   */
  val properties = new NamingFilter[Property[_]](this) {
    def apply[T](name: String) = super.apply(name).asInstanceOf[Property[T]]
  }
}