package org.sgine.ui.internal

import org.sgine.ui.align.{DepthAlignment, VerticalAlignment, HorizontalAlignment}
import org.sgine.ui.Component

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ComponentLocation private[ui](component: Component) extends Property3D("location", component, 0.0, 0.0, 0.0) {
  val actual = new Property3D("actual", this, 0.0, 0.0, 0.0)

  def updateActual() = {
    component.alignment.horizontal() match {
      case HorizontalAlignment.Center => actual.x := x()
      case HorizontalAlignment.Left => actual.x := x() + (component.size.width() / 2.0)
      case HorizontalAlignment.Right => actual.x := x() - (component.size.width() / 2.0)
    }
    component.alignment.vertical() match {
      case VerticalAlignment.Middle => actual.y := y()
      case VerticalAlignment.Top => actual.y := y() - (component.size.height() / 2.0)
      case VerticalAlignment.Bottom => actual.y := y() + (component.size.height() / 2.0)
    }
    component.alignment.depth() match {
      case DepthAlignment.Middle => actual.z := z()
      case DepthAlignment.Front => actual.z := z() + (component.size.depth() / 2.0)
      case DepthAlignment.Back => actual.z := z() - (component.size.depth() / 2.0)
    }
  }
}