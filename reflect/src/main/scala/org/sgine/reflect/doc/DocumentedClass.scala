package org.sgine.reflect.doc

import org.sgine.reflect.EnhancedClass

/**
 * Represents documentation of an EnhancedClass.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class DocumentedClass(name: String, `type` : EnhancedClass, doc: Option[Documentation]) {
  override def toString = if (name != null) {
    name + ": " + `type`.toString
  } else {
    `type`.toString()
  }
}