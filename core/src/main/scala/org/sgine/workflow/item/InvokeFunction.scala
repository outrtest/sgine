package org.sgine.workflow.item

import org.sgine.workflow.WorkflowItem

/**
 * Invokes a function when called in the Workflow.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class InvokeFunction private(f: () => Unit) extends WorkflowItem {
  def act(delta: Double) = {
    f()
    true
  }
}

object InvokeFunction {
  /**
   * Creates an InvokeFunction instance that will call the supplied function upon execution.
   */
  def apply(f: () => Unit) = new InvokeFunction(f)

  /**
   * Convenience method to create an InvokeFunction that will call the supplied function upon
   * execution.
   */
  def when(f: => Unit) = {
    apply(() => f)
  }
}