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
      component.size.width := component.padding.left() + component.size.measured.width() + component.padding.right()
      component.size.height := component.padding.top() + component.size.measured.height() + component.padding.bottom()
      component.size.depth := component.size.measured.depth()
    }
  }

  def aspectRatio(width: Double, height: Double) = new SizeAlgorithm {
    def apply(component: Component) = {
      val ratio = min(width / component.size.measured.width(), height / component.size.measured.height())
      component.size.width := component.size.measured.width() * ratio
      component.size.height := component.size.measured.height() * ratio
      component.size.depth := component.size.measured.depth() * ratio
    }
  }
}