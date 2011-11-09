package org.sgine.ui

/**
 * RenderableComponent is mixed into Components that render something to the screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait RenderableComponent extends Component {
  final def render() = {
    draw()
  }

  protected def draw(): Unit
}