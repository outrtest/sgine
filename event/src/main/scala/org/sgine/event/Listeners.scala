package org.sgine.event

class Listeners(listenable: Listenable) extends Iterable[EventHandler] {
  @volatile
  @transient
  private var handlers: List[EventHandler] = Nil

  def iterator = handlers.iterator

  def +=(handler: EventHandler): EventHandler = synchronized {
    if (!handlers.contains(handler)) {
      handlers = handler :: handlers
    }
    handler
  }

  def +=[E <: Event](f: E => Unit)(implicit manifest: Manifest[E]): EventHandler = this += EventHandler(f)
}