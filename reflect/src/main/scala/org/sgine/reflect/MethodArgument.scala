package org.sgine.reflect

import doc.Documentation

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class MethodArgument(val index: Int,
                     val name: String,
                     val `type` : EnhancedClass,
                     defaultMethod: Option[EnhancedMethod],
                     val doc: Option[Documentation]) {
  def default[T](instance: AnyRef) = defaultMethod.map(m => m(instance).asInstanceOf[T])

  def hasDefault = defaultMethod != None

  override def toString() = name + ": " + `type`
}