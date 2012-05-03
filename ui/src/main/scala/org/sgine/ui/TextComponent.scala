package org.sgine.ui

import font.{BitmapFontGlyph, BitmapFont, TextGenerator}
import org.sgine.property._
import collection.mutable.ListBuffer

/**
 * TextComponent is a base class for all Components needing to draw text to the screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class TextComponent extends ShapeComponent {
  protected[ui] val _text = Property[String]("_text", null)
  protected[ui] val _font = Property[BitmapFont]("_font", null)
  protected[ui] val _wrapWidth = NumericProperty("_wrapWidth", Double.MaxValue)

  protected val generator = new TextGenerator(_font())

  onChange(_text, _font, _wrapWidth) {
    val t = _text()
    val f = _font()
    if (t != null && !t.isEmpty && f != null) {
      generator.font = f
      generator.wrap = _wrapWidth()
      val measuredSize = generator.measure(t)
      size.measured.width := measuredSize._1
      size.measured.height := measuredSize._2
    } else {
      size.measured.width := 0.0
      size.measured.height := 0.0
    }
  }

  private val vertices = new ListBuffer[Double]
  private val coords = new ListBuffer[Double]
  private var tw = 0.0
  private var th = 0.0
  private var offsetX = 0.0
  private var offsetY = 0.0

  private lazy val drawer: (Double, Double, BitmapFontGlyph) => Unit = (x: Double, y: Double, glyph: BitmapFontGlyph) => {
    val vx = offsetX + x + glyph.xOffset
    val vy = offsetY + y + _font().lineHeight - (glyph.yOffset + glyph.height)
    vertices ++= glyph.vertices(vx, vy, 0.0)
    coords ++= glyph.coords(tw, th)
  }

  onRender(_text, _font, _wrapWidth) {
    updateShape()
  }

  private def updateShape() = {
    val t = _text()
    val f = _font()
    if (t != null && !t.isEmpty && f != null) {
      generator.font = _font()
      generator.wrap = _wrapWidth()
      vertices.clear()
      coords.clear()

      offsetX = -(size.measured.width() / 2.0)
      offsetY = (size.measured.height() / 2.0)
      val page = _font().pages.head
      tw = page.texture.getWidth
      th = page.texture.getHeight
      generator.draw(t, drawer)

      _vertices := vertices.toList
      _textureCoordinates := coords.toList
      _texture := page.texture
    }
  }
}