package org.sgine.ui.font

import com.badlogic.gdx.graphics.Texture
import xml.Elem

/**
 * BitmapFontPage represents a page of glyphs for a BitmapFont.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class BitmapFontPage(id: Int, texture: Texture)

object BitmapFontPage {
  protected[ui] def apply(elem: Elem): BitmapFontPage = {
    val id = (elem \ "@id").text.toInt
    val texture = new Texture((elem \ "@file").text)
    BitmapFontPage(id, texture)
  }
}