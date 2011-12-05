package org

package object sgine {
  type Interceptor[T] = (T, T, T) => Option[T]

  implicit def double2Float(value: Double) = value.toFloat
}