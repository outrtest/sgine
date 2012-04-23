package org.sgine

package object workflow {
  val all = Repeat.All
  val first = Repeat.First
  val last = Repeat.Last

  implicit def builder2workflow(builder: WorkflowBuilder) = builder.workflow
}