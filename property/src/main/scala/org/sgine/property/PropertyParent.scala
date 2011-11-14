package org.sgine.property

import org.sgine.naming.{NamingFilter, NamingParent}


/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait PropertyParent extends NamingParent {
  val properties = new NamingFilter[Property[_]](this) {
    def apply[T](name: String) = super.apply(name).asInstanceOf[Property[T]]
  }
}