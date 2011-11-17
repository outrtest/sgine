package org.sgine.workflow

/**
 * Workflow processes child WorkflowItems either synchronously or asynchronously until completion.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Workflow protected(val items: List[WorkflowItem]) extends WorkflowItem {
  protected var currentItems = items
  protected var current: WorkflowItem = _

  def act(delta: Float) = {
    if (current == null && !currentItems.isEmpty) {
      current = currentItems.head
      current.begin()
      currentItems = currentItems.tail
    }
    if (current != null) {
      if (current.act(delta) || current.finished) {
        current.end()
        current = null
      }
      false
    } else {
      true
    }
  }

  override def end() = {
    super.end()
    currentItems = items
    current = null
  }
}