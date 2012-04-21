package org.sgine.ui.font

import xml.Elem
import org.sgine.ui.render.{TextureCoordinates, Vertex}

/**
 * BitmapFontGlyph represents a single character / glyph for a BitmapFont.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class BitmapFontGlyph(id: Int,
    x: Int, y: Int,
    width: Int, height: Int,
    xOffset: Int, yOffset: Int,
    xAdvance: Int, page: Int, channel: Int) {
  /**
   * Generates a quad with the specified offset for this glyph.
   */
  def vertices(x: Double, y: Double, z: Double) = Vertex.quad(x, y + height, x + width, y, z)

  /**
   * Generates the texture coordinates for this glyph based on the texture size supplied.
   */
  def coords(textureWidth: Double, textureHeight: Double) = {
    TextureCoordinates.rectCoords(x, y, width, height, textureWidth, textureHeight)
  }
}

object BitmapFontGlyph {
  protected[font] def apply(elem: Elem): BitmapFontGlyph = {
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