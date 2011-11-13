package org.sgine.workflow.item

import scala.xml.Elem
import org.sgine.workflow.WorkflowItem
import org.sgine.xml.XMLLoader

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class Delay(time: Float, show: Boolean) extends WorkflowItem {
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
    val show = (elem \ "@show").headOption.map(n => n.text.toBoolean).getOrElse(false)
    Delay(time, show)
  }
}