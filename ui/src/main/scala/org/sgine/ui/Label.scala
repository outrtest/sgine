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

  text.onChange {
    updateSize()
  }
  font.onChange {
    updateSize()
  }

  def render() = {
    val text = this.text()
    val font = this.font()
    if (text != null && !text.isEmpty && font != null) {
      font.draw(Component.batch.get(), text, location.x().toFloat, location.y().toFloat)
    }
  }

  private def updateSize() = {
    val text = this.text()
    val font = this.font()
    if (text != null && !text.isEmpty && font != null) {
      val bounds = font.getBounds(text)
      size.width := bounds.width
      size.height := bounds.height
    }
    else {
      size.width := 0.0
      size.height := 0.0
    }
  }
}