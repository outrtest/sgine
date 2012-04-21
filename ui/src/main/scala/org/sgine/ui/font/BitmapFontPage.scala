package org.sgine.ui.font

import com.badlogic.gdx.graphics.Texture
import xml.Elem
import com.badlogic.gdx.Gdx

/**
 * BitmapFontPage represents a page of glyphs for a BitmapFont.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class BitmapFontPage(id: Int, texture: Texture)

object BitmapFontPage {
  protected[ui] def apply(elem: Elem): BitmapFontPage = {
    val id = (elem \ "@id").text.toInt
    val texture = new Texture(Gdx.files.internal((elem \ "@file").text), false)
    BitmapFontPage(id, texture)
  }
}