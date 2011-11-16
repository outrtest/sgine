package org.sgine.ui

import font.{BitmapFontGlyph, BitmapFont, TextGenerator}
import org.sgine.property._
import collection.mutable.ListBuffer

/**
 * 
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class TextComponent extends ShapeComponent {
  protected[ui] val _text = Property[String]()
  protected[ui] val _font = Property[BitmapFont]()
  protected[ui] val _wrapWidth = Property[Double](Double.MaxValue)

  protected val generator = new TextGenerator(_font())

  object measured extends PropertyParent {
    val width = Property[Double](0.0)
    val height = Property[Double](0.0)
  }

  onChange(_text, _font, _wrapWidth) {
    val t = _text()
    val f = _font()
    if (t != null && !t.isEmpty && f != null) {
      generator.font = f
      generator.wrap = _wrapWidth()
      val size = generator.measure(t)
      measured.width := size._1
      measured.height := size._2
    } else {
      measured.width := 0.0
      measured.height := 0.0
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

  def updateShape() = {
    val t = _text()
    val f = _font()
    if (t != null && !t.isEmpty && f != null) {
      generator.font = _font()
      generator.wrap = _wrapWidth()
      vertices.clear()
      coords.clear()

      offsetX = -(measured.width() / 2.0)
      offsetY = (measured.height() / 2.0)
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