package org.sgine.ui

import scala.math._

/**
 * SizeAlgorithm can be set on Component.size.algorithm in order update the actual size when the
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait SizeAlgorithm {
  def apply(component: Component): Unit
}

object SizeAlgorithm {
  val measured = new SizeAlgorithm {
    def apply(component: Component) = {
      component.size.width := component.padding.left() + component.measured.width() + component.padding.right()
      component.size.height := component.padding.top() + component.measured.height() + component.padding.bottom()
      component.size.depth := component.measured.depth()
    }
  }

  def aspectRatio(width: Double, height: Double) = new SizeAlgorithm {
    def apply(component: Component) = {
      val ratio = min(width / component.measured.width(), height / component.measured.height())
      component.size.width := component.measured.width() * ratio
      component.size.height := component.measured.height() * ratio
      component.size.depth := component.measured.depth() * ratio
    }
  }
}