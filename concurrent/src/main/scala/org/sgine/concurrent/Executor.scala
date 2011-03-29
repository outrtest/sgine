package org.sgine.concurrent

import java.util.concurrent.{Callable, Executors}

object Executor {
  private lazy val executor = Executors.newCachedThreadPool()

  def future[T](f: () => T) = executor.submit(new C(f))

  def execute[T](f: () => T): Unit = execute(new R(f))

  def execute(r: Runnable): Unit = executor.execute(r)

  class C[T](f: () => T) extends Callable[T] {
    def call = f()
  }

  class R[T](f: () => T) extends Runnable {
    def run = f()
  }
}