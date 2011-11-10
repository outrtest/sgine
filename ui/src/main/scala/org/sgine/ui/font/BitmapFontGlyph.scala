package org.sgine.ui.font

import xml.Elem

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class BitmapFontGlyph(id: Int,
    x: Int, y: Int,
    width: Int, height: Int,
    xOffset: Int, yOffset: Int,
    xAdvance: Int, page: Int, channel: Int)

object BitmapFontGlyph {
  def apply(elem: Elem): BitmapFontGlyph = {
    def ai(name: String) = (elem \ ("@" + name)).text.toInt

    val id = ai("id")
    val x = ai("x")
    val y = ai("y")
    val width = ai("width")
    val height = ai("height")
    val xOffset = ai("xoffset")
    val yOffset = ai("yoffset")
    val xAdvance = ai("xadvance")
    val page = ai("page")
    val channel = ai("chnl")

    BitmapFontGlyph(id, x, y, width, height, xOffset, yOffset, xAdvance, page, channel)
  }
}