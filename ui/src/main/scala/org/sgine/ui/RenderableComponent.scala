package org.sgine.ui

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.Gdx

/**
 * RenderableComponent is mixed into Components that render something to the screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait RenderableComponent extends Component {
  private val matrix = new Matrix4()

  onUpdate(location.x, location.y, location.z) {
    matrix.idt()
    matrix.translate(location.x().toFloat, location.y().toFloat, location.z().toFloat)
  }

  final def render() = {
    UI().camera()(Gdx.gl11)
    Gdx.gl11.glMultMatrixf(matrix.getValues, 0)
    draw()
  }

  protected def draw(): Unit
}