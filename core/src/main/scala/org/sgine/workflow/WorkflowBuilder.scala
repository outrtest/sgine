package org.sgine.workflow

import item.Delay

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class WorkflowBuilder(currentItems: List[WorkflowItem] = Nil,
                           workflowItems: List[WorkflowItem] = Nil,
                           loopCount: Int = 0) {
  def pause(time: Double) = nextStep().copy(currentItems = List(Delay(time))).nextStep()

  def delay(time: Double) = pause(time)

  def loop(count: Int) = copy(loopCount = count)

  def repeat(r: Repeat) = {
    val b = nextStep()
    r match {
      case Repeat.All => b.copy(workflowItems = b.workflowItems ::: b.workflowItems)
      case Repeat.First => b.copy(workflowItems = b.workflowItems.last :: b.workflowItems)
      case Repeat.Last => b.copy(workflowItems = b.workflowItems.head :: b.workflowItems)
    }
  }

  def nextStep() = {
    val items = if (currentItems.nonEmpty) {
      AsynchronousWorkflow(currentItems) :: workflowItems
    } else {
      workflowItems
    }
    copy(currentItems = Nil, workflowItems = items)
  }

  def workflow = {
    val builder = nextStep()
    new Workflow(builder.workflowItems.reverse) with Looping {
      val loops = loopCount
    }
  }
}

case class AsynchronousWorkflow(workflowItems: List[WorkflowItem]) extends Workflow(workflowItems) with Asynchronous