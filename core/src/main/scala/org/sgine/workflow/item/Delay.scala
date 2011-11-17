package org.sgine.workflow.item

import scala.xml.Elem
import org.sgine.workflow.WorkflowItem
import org.sgine.xml.XMLLoader

/**
 * Delays before completing for the time specified.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class Delay(time: Float) extends WorkflowItem {
  private var elapsed: Float = _

  override def begin() = {
    super.begin()
    elapsed = 0.0f
  }

  def act(delta: Float) = {
    elapsed += delta
    elapsed >= time
  }

  override def end() = {
    super.end()
    elapsed = time
  }
}

object Delay extends XMLLoader[Delay] {
  def apply(elem: Elem) = {
    val time = elem.text.toFloat
    Delay(time)
  }
}