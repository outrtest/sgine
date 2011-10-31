package org.sgine.ui

import org.sgine.scene.MutableContainer

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Container extends MutableContainer[Component] with Component {
  def render() = {
    if (visible()) {
      contents.foreach(Container.renderComponent)
    }
  }
}

object Container {
  private val renderComponent = (component: Component) => {
    component.render()
  }
}