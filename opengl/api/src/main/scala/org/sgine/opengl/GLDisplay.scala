package org.sgine.opengl

trait GLDisplay {
  def create(): Unit

  def pause(): Unit

  def resume(): Unit

  def resize(width: Int, height: Int): Unit

  def render(): Unit

  def dispose(): Unit
}