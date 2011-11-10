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
    spacing: String,
    lineHeight: Int,
    base: Int,
    scaleW: Int,
    scaleH: Int,
    pages: List[BitmapFontPage],
    glyphs: Map[Int, BitmapFontGlyph],
    kernings: Map[(Int, Int), BitmapFontKerning],
    packed: Int) {
  def text(text: String, shape: ShapeComponent, kerning: Boolean = true) = {
    val vertices = new ListBuffer[Double]
    val coords = new ListBuffer[Double]
    var x = 0.0
    val page = pages.head // TODO: support multiple pages
    var p: BitmapFontGlyph = null
    for (c <- text) {
      val glyph = glyphs(c)
      vertices ++= glyph.vertices(x + glyph.xOffset, 0.0, 0.0)
      coords ++= glyph.coords(page.texture.getWidth, page.texture.getHeight)
      x += glyph.xAdvance
      if (kerning && p != null) {
        x += this.kerning(p.id, c)
      }
      p = glyph
    }
    shape.vertices := vertices.toList
    shape.textureCoordinates := coords.toList
    shape.texture := page.texture
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
    val spacing = (info \ "@spacing").text
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
    BitmapFont(face, size, bold, italic, charset, unicode, stretchH, smooth, aa, padding, spacing,
      lineHeight, base, scaleW, scaleH, pages, glyphs, kerning, packed)
  }
}