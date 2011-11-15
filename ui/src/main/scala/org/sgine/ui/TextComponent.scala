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
  protected[ui] val _wrapWidth = Property[Double](0.0)

  protected val generator = new TextGenerator(_font())

  object measured extends PropertyParent {
    val width = Property[Double](0.0)
    val height = Property[Double](0.0)
  }

  onChange(_text, _font, _wrapWidth) {
    val t = _text()
    val f = _font()
    if (t != null && !t.isEmpty) {
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
    vertices ++= glyph.vertices(offsetX + x + glyph.xOffset, offsetY + y, 0.0)
    coords ++= glyph.coords(tw, th)
  }

  @volatile private var us = false

  onUpdate(_text, _font, _wrapWidth) {
    us = true   // TODO: figure out why I can't update the shapecomponent during update
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

  /*
  val font = BitmapFont(Resource("arial64.fnt"))
  val shape = new ShapeComponent()
  val tg = new TextGenerator(font)
  tg.wrap = 100.0
  val text = "Hello World!"

  val (mx, my) = tg.measure(text)
  println("Measure: " + mx + "x" + my)
  val vertices = new ListBuffer[Double]
  val coords = new ListBuffer[Double]
  var offsetX = -(mx / 2.0)
  var offsetY = (my / 2.0)
  val page = font.pages.head
  val f: (Double, Double, BitmapFontGlyph) => Unit = (x: Double, y: Double,
      glyph: BitmapFontGlyph) => {
    vertices ++= glyph.vertices(offsetX + x + glyph.xOffset, offsetY + y, 0.0)
    coords ++= glyph.coords(page.texture.getWidth, page.texture.getHeight)
  }
  tg.draw(text, f)
  shape.vertices := vertices.toList
  shape.textureCoordinates := coords.toList
  shape.texture := page.texture
   */
  override protected def draw() = {
    if (us) {
      updateShape()
      us = false
    }

    super.draw()
  }
}