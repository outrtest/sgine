package org.sgine.ui.font

import xml.Elem

/**
 * BitmapFontKerning represents the kerning value between two characters on a BitmapFont.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class BitmapFontKerning(first: Int, second: Int, amount: Int)

object BitmapFontKerning {
  protected[font] def apply(elem: Elem): BitmapFontKerning = {
    val first = (elem \ "@first").text.toInt
    val second = (elem \ "@second").text.toInt
    val amount = (elem \ "@amount").text.toInt
    BitmapFontKerning(first, second, amount)
  }
}