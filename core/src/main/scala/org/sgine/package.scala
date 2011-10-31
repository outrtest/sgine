package org

package object sgine {
  type Interceptor[T] = (T, T, T) => Option[T]

  type Listener[T] = (T, T) => Unit
}