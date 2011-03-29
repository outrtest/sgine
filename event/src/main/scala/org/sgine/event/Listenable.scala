package org.sgine.event

trait Listenable {
  def parent: Listenable = null

  val listeners = new Listeners(this)
}