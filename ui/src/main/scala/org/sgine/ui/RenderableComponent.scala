package org.sgine.ui

import com.badlogic.gdx.Gdx
import org.sgine.AsynchronousInvocation
import org.sgine.event.{ChangeEvent, Listenable}

/**
 * RenderableComponent is mixed into Components that render something to the screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait RenderableComponent extends Component {
  private val renderAsync = new AsynchronousInvocation()

  /**
   * This final method updates the screen coordinates for the Component's matrix, invokes all
   * onRender listeners, and calls the draw method.
   */
  final def render() = {
    ui match {
      case null => // Rendering outside of UI
      case root => root.camera()(Gdx.gl11)
    }
    Gdx.gl11.glMultMatrixf(matrix.getValues, 0)
    Gdx.gl11.glColor4f(color.actual.red().toFloat, color.actual.green().toFloat, color.actual.blue().toFloat, color.actual.alpha().toFloat)
    renderAsync.invokeNow()
    draw()
  }

  /**
   * This method is called by render() and allows the specific Component implementation to define
   * how it should be drawn to the screen. The coordinates will already be updated.
   */
  protected def draw(): Unit

  /**
   * Adds change listeners to the Listenables to invoke the supplied function during the next render
   * cycle.
   */
  def onRender(listenables: Listenable*)(f: => Unit) = {
    val function = () => f
    listenables.foreach(l => l.listeners.synchronous {
      case event: ChangeEvent[_] => renderAsync.invokeLater(function)
    })
  }
}