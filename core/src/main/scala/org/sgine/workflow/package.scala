package org.sgine

import workflow.item.InvokeFunction

package object workflow {
  val all = Repeat.All
  val first = Repeat.First
  val last = Repeat.Last

  implicit def builder2workflow(builder: WorkflowBuilder) = builder.workflow

  def invoke(f: => Any) = InvokeFunction(() => f)
}