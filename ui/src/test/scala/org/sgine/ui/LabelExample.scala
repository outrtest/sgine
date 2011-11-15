package org.sgine.ui

import font.{BitmapFontGlyph, TextGenerator, BitmapFont}
import org.sgine.Resource
import collection.mutable.ListBuffer

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object LabelExample extends UI {
  contents += Image("sgine.png")

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

  contents += shape
}