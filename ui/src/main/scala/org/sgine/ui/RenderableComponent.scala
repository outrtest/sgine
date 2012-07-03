package org.sgine.ui

import com.badlogic.gdx.Gdx
import org.powerscala.AsynchronousInvocation
import org.powerscala.event.{ChangeEvent, Listenable}
import org.powerscala.concurrent.Time
import org.powerscala.property.Property

/**
 * RenderableComponent is mixed into Components that render something to the screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait RenderableComponent extends Component {
  private val renderAsync = new AsynchronousInvocation()

  /**
   * Defines whether the camera positioning should be used to determine rendering placement of this component.
   *
   * Defaults to true.
   */
  val useCamera = Property[Boolean]("useCamera", true)    // TODO: make this work correctly

  /**
   * This final method updates the screen coordinates for the Component's matrix, invokes all
   * onRender listeners, and calls the draw method.
   */
  final def render() = {
    renderAsync.thread = Thread.currentThread()
    ui match {
      case root: UI if (useCamera()) => root.camera()(Gdx.gl11)
      case _ => Gdx.gl11.glLoadIdentity()
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
      case event: ChangeEvent => renderAsync.invokeLater(function)
    })
  }

  def invokeAtRender(f: => Unit) = {
    val function = () => f
    renderAsync.invokeLater(function)
  }

  def invokeAtRenderAndWait(f: => Unit, time: Double = Double.MaxValue) = {
    var finished = false
    val function = () => {
      f
      finished = true
    }
    renderAsync.invokeLater(function)
    Time.waitFor(time) {
      finished
    }
  }
}