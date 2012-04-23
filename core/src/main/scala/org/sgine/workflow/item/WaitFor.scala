package org.sgine.workflow.item

import org.sgine.workflow.WorkflowItem

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class WaitFor(condition: () => Boolean) extends WorkflowItem {
  def act(delta: Double) = condition()
}
