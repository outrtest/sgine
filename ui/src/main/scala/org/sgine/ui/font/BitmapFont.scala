package org.sgine.ui.font

import org.sgine.Resource
import xml.{Elem, XML}
import org.sgine.ui.ShapeComponent
import collection.mutable.ListBuffer

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class BitmapFont(face: String,
    size: Int,
    bold: Int,
    italic: Int,
    charset: String,
    unicode: Int,
    stretchH: Int,
    smooth: Int,
    aa: Int,
    padding: String,
    spacing: Array[Int],
    lineHeight: Int,
    base: Int,
    scaleW: Int,
    scaleH: Int,
    pages: List[BitmapFontPage],
    glyphs: Map[Int, BitmapFontGlyph],
    kernings: Map[(Int, Int), BitmapFontKerning],
    packed: Int) {
  def text(text: String, shape: ShapeComponent, kerning: Boolean = true) = {
    val (mx, my) = measure(text, kerning)
    val vertices = new ListBuffer[Double]
    val coords = new ListBuffer[Double]
    var offsetX = -(mx / 2.0)
    var offsetY = -(my / 2.0)
    val page = pages.head
    val f: (Double, Double, BitmapFontGlyph) => Unit = (x: Double, y: Double,
        glyph: BitmapFontGlyph) => {
      vertices ++= glyph.vertices(offsetX + x + glyph.xOffset, offsetY + y, 0.0)
      coords ++= glyph.coords(page.texture.getWidth, page.texture.getHeight)
    }
    process(text, kerning, f)
    shape.vertices := vertices.toList
    shape.textureCoordinates := coords.toList
    shape.texture := page.texture
  }

  def measure(text: String, kerning: Boolean = true) = process(text, kerning)

  def process(text: String, kerning: Boolean = true,
      f: (Double, Double, BitmapFontGlyph) => Unit = null) = {
    var x = 0.0
    var y = 0.0
    var p: BitmapFontGlyph = null
    for (c <- text) {
      val glyph = glyphs(c)
      if (kerning && p != null) {
        x += this.kerning(p.id, c)
      }
      if (f != null) {
        f(x, y, glyph)
      }
      x += glyph.xAdvance
    }
    x -> lineHeight
  }

  def kerning(first: Int, second: Int) = kernings.get(first -> second).map(k => k.amount)
      .getOrElse(0)
}

object BitmapFont {
  // TODO: support packed font
  def apply(resource: Resource): BitmapFont = {
    val xml = XML.load(resource.handle.read())
    val info = (xml \ "info").head
    val face = (info \ "@face").text
    val size = (info \ "@size").text.toInt
    val bold = (info \ "@bold").text.toInt
    val italic = (info \ "@italic").text.toInt
    val charset = (info \ "@bold").text
    val unicode = (info \ "@unicode").text.toInt
    val stretchH = (info \ "@stretchH").text.toInt
    val smooth = (info \ "@smooth").text.toInt
    val aa = (info \ "@aa").text.toInt
    val padding = (info \ "@padding").text
    val spacing = (info \ "@spacing").text.split(",").map(s => s.trim.toInt)
    val common = (xml \ "common").head
    val lineHeight = (common \ "@lineHeight").text.toInt
    val base = (common \ "@base").text.toInt
    val scaleW = (common \ "@scaleW").text.toInt
    val scaleH = (common \ "@scaleH").text.toInt
    val packed = (common \ "@packed").text.toInt
    val pages = (xml \ "pages" \ "page").map(n => BitmapFontPage(n.asInstanceOf[Elem])).toList
    val glyphs = (xml \ "chars" \ "char").map(n => BitmapFontGlyph(n.asInstanceOf[Elem]))
        .map(g => g.id -> g).toMap
    val kerning = (xml \ "kernings" \ "kerning").map(n => BitmapFontKerning(n.asInstanceOf[Elem]))
        .map(k => (k.first, k.second) -> k).toMap
    if (pages.length != 1) {
      throw new RuntimeException("No support for multiple pages yet.")
    }
    BitmapFont(face, size, bold, italic, charset, unicode, stretchH, smooth, aa, padding, spacing,
      lineHeight, base, scaleW, scaleH, pages, glyphs, kerning, packed)
  }
}

class TextGenerator(font: BitmapFont, kerning: Boolean = true) {
  type Drawer = (Double, Double, BitmapFontGlyph) => Unit

  private var x = 0.0
  private var y = 0.0
  private val b = new StringBuilder
  private var drawer: Drawer = _
  private var wrap: Double = _

  def process(text: String, drawer: Drawer, wrap: Double) = {
    // Reset
    x = 0.0
    y = 0.0
    b.clear()
    this.drawer = drawer
    this.wrap = wrap

    // Process text
    for (c <- text) {
      if (c == '\n') {
        drawCurrent()
        drawNewLine()
      }
      else if (c.isWhitespace) {
        drawCurrent()
      }
    }
  }

  def drawCurrent() = if (!b.isEmpty) {
    val (width, _) = font.measure(b.toString(), kerning)
    if (width > wrap) {
      drawNewLine()
    }
    font.process(b.toString(), kerning, drawer)
    x += width
    b.clear()
  }

  def drawNewLine() = {
    x = 0.0
    y += font.lineHeight
  }
}