package org.sgine.workflow

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait WorkflowItem {
  protected[workflow] var id: String = _
  protected[workflow] var finished = false

  def begin() = {
    finished = false
    if (id != null) {
      WorkflowItem.register(id, this)
    }
  }

  /**
   * Acts on the workflow item returning true if it's completed.
   */
  def act(delta: Float): Boolean

  def end() = {
    if (id != null) {
      WorkflowItem.unregister(id)
    }
  }

  def stop() = {
    finished = true
  }
}

object WorkflowItem {
  private var items = Map.empty[String, WorkflowItem]

  private def register(id: String, item: WorkflowItem) = synchronized {
    items += id -> item
  }

  private def unregister(id: String) = synchronized {
    items -= id
  }

  def apply(id: String) = items(id)
}