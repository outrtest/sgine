package org.sgine.property

import org.sgine.workflow.WorkflowBuilder

package object workflow {
  implicit def wb2pab(b: WorkflowBuilder) = new PropertyAnimatorBuilder(b)

  implicit def p2pab(p: MutableProperty[Double]) = new PropertyAnimatorBuilder(new WorkflowBuilder().and(p))
}