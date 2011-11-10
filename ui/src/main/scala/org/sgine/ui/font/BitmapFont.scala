package org.sgine.ui.font

import com.badlogic.gdx.graphics.Texture
import org.sgine.Resource
import xml.{Elem, XML}

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
    glyphs: List[BitmapFontGlyph],
    kerning: List[BitmapFontKerning],
    packed: Int)

case class BitmapFontPage(id: Int, texture: Texture)

object BitmapFontPage {
  def apply(elem: Elem): BitmapFontPage = {
    val id = (elem \ "@id").text.toInt
    val texture = new Texture((elem \ "@file").text)
    BitmapFontPage(id, texture)
  }
}

case class BitmapFontKerning(first: Int, second: Int, amount: Int)

object BitmapFontKerning {
  def apply(elem: Elem): BitmapFontKerning = {
    val first = (elem \ "@first").text.toInt
    val second = (elem \ "@second").text.toInt
    val amount = (elem \ "@amount").text.toInt
    BitmapFontKerning(first, second, amount)
  }
}

object BitmapFont {
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
    val glyphs = (xml \ "chars" \ "char").map(n => BitmapFontGlyph(n.asInstanceOf[Elem])).toList
    val kerning = (xml \ "kernings" \ "kerning").map(n => BitmapFontKerning(n.asInstanceOf[Elem]))
        .toList
    BitmapFont(face, size, bold, italic, charset, unicode, stretchH, smooth, aa, padding, spacing,
      lineHeight, base, scaleW, scaleH, pages, glyphs, kerning, packed)
  }
}