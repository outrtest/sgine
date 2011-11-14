package org.sgine.ui.font

import xml.Elem

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class BitmapFontKerning(first: Int, second: Int, amount: Int)

object BitmapFontKerning {
  def apply(elem: Elem): BitmapFontKerning = {
    val first = (elem \ "@first").text.toInt
    val second = (elem \ "@second").text.toInt
    val amount = (elem \ "@amount").text.toInt
    BitmapFontKerning(first, second, amount)
  }
}