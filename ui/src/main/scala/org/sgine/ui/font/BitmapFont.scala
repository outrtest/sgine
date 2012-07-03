package org.sgine.ui.font

import org.powerscala.Resource
import org.sgine.ui._
import xml.{Elem, XML}

/**
 * BitmapFont represents a font that utilizes vertices and textures to display.
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

  /**
   * Determines kerning for the space between first and second or 0 if none.
   */
  def kerning(first: Int, second: Int) = {
    kernings.get(first -> second).map(k => k.amount).getOrElse(0)
  }
}

object BitmapFont {
  // TODO: support packed font
  /**
   * Loads a BitmapFont from the resource supplied.
   *
   * Currently only works with XML defined fonts.
   */
  def apply(resource: Resource): BitmapFont = {
    // TODO: cache in UI
    val xml = XML.load(resource.read())
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