package org.sgine.ui

import com.badlogic.gdx.Gdx
import org.sgine.{Listenable, AsynchronousInvocation}

/**
 * RenderableComponent is mixed into Components that render something to the screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait RenderableComponent extends Component {
  private val renderAsync = new AsynchronousInvocation()

  final def render() = {
    UI().camera()(Gdx.gl11)
    Gdx.gl11.glMultMatrixf(matrix.getValues, 0)
    renderAsync.invokeNow()
    draw()
  }

  protected def draw(): Unit

  def onRender(listenables: Listenable[_]*)(f: => Unit) = {
    val function = () => f
    val listener = (oldValue: Any, newValue: Any) => renderAsync.invokeLater(function)
    listenables.foreach(l => l.listeners += listener)
  }
}