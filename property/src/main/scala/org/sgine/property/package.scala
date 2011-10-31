package org.sgine

package object property {
  implicit def propertyToValue[T](property: Property[T]) = property.value
}