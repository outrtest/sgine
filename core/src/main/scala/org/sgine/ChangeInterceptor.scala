package org.sgine

/**
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait ChangeInterceptor[T] {
  private var _interceptors: List[Interceptor[T]] = Nil

  object interceptors {
    def +=(interceptor: Interceptor[T]) = synchronized {
      _interceptors = interceptor :: _interceptors
    }

    def -=(interceptor: Interceptor[T]) = synchronized {
      _interceptors = _interceptors.filterNot(i => i eq interceptor)
    }
  }

  def change(oldValue: T, newValue: T): T = {
    _interceptors.foldLeft(newValue)((current, interceptor) => interceptor(oldValue, newValue, current).getOrElse(current))
  }
}