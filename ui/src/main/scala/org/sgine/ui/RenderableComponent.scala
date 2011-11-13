package org.sgine.ui

import com.badlogic.gdx.Gdx

/**
 * RenderableComponent is mixed into Components that render something to the screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait RenderableComponent extends Component {
  final def render() = {
    UI().camera()(Gdx.gl11)
    Gdx.gl11.glMultMatrixf(matrix.getValues, 0)
    draw()
  }

  protected def draw(): Unit
}