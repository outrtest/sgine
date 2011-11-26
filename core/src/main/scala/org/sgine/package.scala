package org

package object sgine {
  type Interceptor[T] = (T, T, T) => Option[T]

  type Listener[T] = (T, T) => Unit

  implicit def double2Float(value: Double) = value.toFloat
}