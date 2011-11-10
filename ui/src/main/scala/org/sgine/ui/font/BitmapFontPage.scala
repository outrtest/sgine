package org.sgine.ui.font

import com.badlogic.gdx.graphics.Texture
import xml.Elem

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class BitmapFontPage(id: Int, texture: Texture)

object BitmapFontPage {
  def apply(elem: Elem): BitmapFontPage = {
    val id = (elem \ "@id").text.toInt
    val texture = new Texture((elem \ "@file").text)
    BitmapFontPage(id, texture)
  }
}