package org.sgine.workflow.item

import org.sgine.workflow.WorkflowItem

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class InvokeFunction private(f: () => Unit) extends WorkflowItem {
  def act(delta: Float) = {
    f()
    true
  }
}

object InvokeFunction {
  def apply(f: () => Unit) = new InvokeFunction(f)

  def when(f: => Unit) = {
    apply(() => f)
  }
}