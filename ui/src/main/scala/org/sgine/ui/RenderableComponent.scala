package org.sgine.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * RenderableComponent is mixed into Components that render something to the screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait RenderableComponent extends Component {
  final def render() = {
    val batch = Component.batch.get()
    draw(batch)
  }

  protected def draw(batch: SpriteBatch): Unit
}