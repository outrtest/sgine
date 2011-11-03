package org.sgine.ui

import org.sgine.property.Property
import com.badlogic.gdx.graphics.g2d.BitmapFont

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Label extends RenderableComponent {
  val text = Property[String]()
  val font = Property[BitmapFont]()
  val wrapWidth = Property[Double](0.0)

  text.onChange {
    updateSize()
  }
  font.onChange {
    updateSize()
  }
  wrapWidth.onChange {
    updateSize()
  }

  def render() = {
    val text = this.text()
    val font = this.font()
    if (text != null && !text.isEmpty && font != null) {
      if (wrapWidth() > 0.0) {
        font.drawWrapped(Component.batch.get(), text, location.x().toFloat,
          location.y().toFloat + size.height().toFloat, wrapWidth().toFloat)
      }
      else {
        font.draw(Component.batch.get(), text, location.x().toFloat,
          location.y().toFloat + size.height().toFloat)
      }
    }
  }

  private def updateSize() = {
    val text = this.text()
    val font = this.font()
    if (text != null && !text.isEmpty && font != null) {
      val wrapWidth = this.wrapWidth()
      val bounds = if (wrapWidth > 0.0) {
        font.getWrappedBounds(text, wrapWidth.toFloat)
      }
      else {
        font.getBounds(text)
      }
      size.width := bounds.width
      size.height := bounds.height
    }
    else {
      size.width := 0.0
      size.height := 0.0
    }
  }
}