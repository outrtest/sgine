/*
 * Copyright (c) 2011 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.math

import annotation.tailrec
import org.sgine.{EnumEntry, Enumerated}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Color extends Traversable[Double] with EnumEntry {
  def red: Double
  def green: Double
  def blue: Double
  def alpha: Double

  def apply(index: Int) = index match {
    case 0 => red
    case 1 => green
    case 2 => blue
    case 3 => alpha
    case _ => throw new IndexOutOfBoundsException("Index " + index + " is greater than Color bounds (3)")
  }

  def apply(
            red: Double = this.red,
            green: Double = this.green,
            blue: Double = this.blue,
            alpha: Double = this.alpha
             ): Color

  def apply(c: Color): Color = apply(c.red, c.green, c.blue, c.alpha)

  def foreach[U](f: Double => U) = forIndexed(0, f)

  @tailrec
  private def forIndexed[U](index: Int, f: Double => U): Unit = {
    f(apply(index))
    if (index < 3) forIndexed(index + 1, f)
  }

  override val size = 4

  def toMutable: org.sgine.math.mutable.MutableColor
  def toImmutable: org.sgine.math.immutable.ImmutableColor

  def add(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 0.0) = {
    apply(this.red + red, this.green + green, this.blue + blue, this.alpha + alpha)
  }

  def subtract(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 0.0) = {
    apply(this.red - red, this.green - green, this.blue - blue, this.alpha - alpha)
  }

  def multiply(red: Double = 1.0, green: Double = 1.0, blue: Double = 1.0, alpha: Double = 1.0) = {
    apply(this.red * red, this.green * green, this.blue * blue, this.alpha * alpha)
  }

  def divide(red: Double = 1.0, green: Double = 1.0, blue: Double = 1.0, alpha: Double = 1.0) = {
    apply(this.red / red, this.green / green, this.blue / blue, this.alpha / alpha)
  }

  override def equals(that: Any) = that match {
    case color: Color => color.red == red && color.green == green && color.blue == blue && color.alpha == alpha
    case _ => false
  }

  override def toString() = "Color(r=" + red + ", g=" + green + ", b=" + blue + ", a=" + alpha + ")"
}

object Color extends Enumerated[Color] {
  val AliceBlue = immut(0xfff0f8ff)
	val AntiqueWhite = immut(0xfffaebd7)
	val Aquamarine = immut(0xff7fffd4)
	val Azure = immut(0xfff0fff)
	val Beige = immut(0xfff5f5dc)
	val Bisque = immut(0xffffe4c4)
	val Black = immut(0xff000000)
	val BlanchedAlmond = immut(0xffffebcd)
	val Blue = immut(0xff0000ff)
	val BlueViolet = immut(0xff8a2be2)
	val Brown = immut(0xffa52a2a)
	val Burlywood = immut(0xffdeb887)
	val CadetBlue = immut(0xff5f9ea0)
	val Chartreuse = immut(0xff7fff00)
	val Chocolate = immut(0xffd2691e)
	val Coral = immut(0xffff7f50)
	val CornflowerBlue = immut(0xff6495ed)
	val Cornsilk = immut(0xfffff8dc)
	val Cyan = immut(0xff00cdcd)
	val DarkBlue = immut(0xff00008b)
	val DarkGoldenrod = immut(0xffb8860b)
	val DarkGray = immut(0xff3f3f3f)
	val DarkGreen = immut(0xff006400)
	val DarkKhaki = immut(0xffbdb76b)
	val DarkOliveGreen = immut(0xff556b2f)
	val DarkOrange = immut(0xffff8c00)
	val DarkOrchid = immut(0xff9932cc)
	val DarkRed = immut(0xff8b0000)
	val DarkSalmon = immut(0xffe9967a)
	val DarkSeaGreen = immut(0xff8fbc8f)
	val DarkSlateBlue = immut(0xff483d8b)
	val DarkSlateGray = immut(0xff2f4f4f)
	val DarkTurquoise = immut(0xff00ced1)
	val DarkViolet = immut(0xff9400d3)
	val DeepPink = immut(0xffff1493)
	val DeepSkyBlue = immut(0xff00bfff)
	val DimGray = immut(0xff696969)
	val DodgerBlue = immut(0xff1e90f)
	val Firebrick = immut(0xffb22222)
	val FloralWhite = immut(0xfffffaf0)
	val ForestGreen = immut(0xff228b22)
	val Gainsboro = immut(0xffdcdcdc)
	val GhostWhite = immut(0xfff8f8ff)
	val Gold = immut(0xffffd700)
	val Goldenrod = immut(0xffdaa520)
	val Gray = immut(0xffbebebe)
	val Green = immut(0xff00ff00)
	val GreenYellow = immut(0xffadff2f)
	val HaloBlue = immut(0xff93a9b4)
	val HighlightBlue = immut(0xffb2e1ff)
	val Honeydew = immut(0xfff0ff0)
	val HotPink = immut(0xffff69b4)
	val IndianRed = immut(0xffcd5c5c)
	val Ivory = immut(0xfffffff0)
	val Khaki = immut(0xfff0e68c)
	val Lavender = immut(0xffe6e6fa)
	val LavenderBlush = immut(0xfffff05)
	val LawnGreen = immut(0xff7cfc00)
	val LemonChiffon = immut(0xfffffacd)
	val LightBlue = immut(0xffadd8e6)
	val LightCoral = immut(0xfff08080)
	val LightCyan = immut(0xffe0fff)
	val LightGoldenrod = immut(0xffeedd82)
	val LightGoldenrodYellow = immut(0xfffafad2)
	val LightGray = immut(0xffd3d3d3)
	val LightPink = immut(0xffffb6c1)
	val LightSalmon = immut(0xffffa07a)
	val LightSeaGreen = immut(0xff20b2aa)
	val LightSkyBlue = immut(0xff87cefa)
	val LightSlateBlue = immut(0xff8470f)
	val LightSlateGray = immut(0xff778899)
	val LightSteelBlue = immut(0xffb0c4de)
	val LightYellow = immut(0xffffffe0)
	val LimeGreen = immut(0xff32cd32)
	val Linen = immut(0xfffaf0e6)
	val Magenta = immut(0xffff00ff)
	val Maroon = immut(0xffb03060)
	val MediumAquamarine = immut(0xff66cdaa)
	val MediumBlue = immut(0xff0000cd)
	val MediumOrchid = immut(0xffba55d3)
	val MediumPurple = immut(0xff9370db)
	val MediumSeaGreen = immut(0xff3cb371)
	val MediumSlateBlue = immut(0xff7b68ee)
	val MediumSpringGreen = immut(0xff00a9a)
	val MediumTurquoise = immut(0xff48d1cc)
	val MediumVioletRed = immut(0xffc71585)
	val MidnightBlue = immut(0xff191970)
	val MintCream = immut(0xfff5fffa)
	val MistyRose = immut(0xffffe4e1)
	val Moccasin = immut(0xffffe4b5)
	val NavajoWhite = immut(0xffffdead)
	val NavyBlue = immut(0xff000080)
	val OldLace = immut(0xfffdf5e6)
	val OliveDrab = immut(0xff6b8e23)
	val Orange = immut(0xffffa500)
	val OrangeRed = immut(0xffff4500)
	val Orchid = immut(0xffda70d6)
	val PaleGoldenrod = immut(0xffeee8aa)
	val PaleGreen = immut(0xff98fb98)
	val PaleTurquoise = immut(0xffafeeee)
	val PaleVioletRed = immut(0xffdb7093)
	val PapayaWhip = immut(0xffffefd5)
	val PeachPuff = immut(0xffffdab9)
	val Peru = immut(0xffcd853f)
	val Pink = immut(0xffffc0cb)
	val Plum = immut(0xffdda0dd)
	val PowderBlue = immut(0xffb0e0e6)
	val Purple = immut(0xffa0200)
	val Red = immut(0xffff0000)
	val RosyBrown = immut(0xffbc8f8f)
	val RoyalBlue = immut(0xff4169e1)
	val SaddleBrown = immut(0xff8b4513)
	val Salmon = immut(0xfffa8072)
	val SandyBrown = immut(0xfff4a460)
	val SeaGreen = immut(0xff2e8b57)
	val Seashell = immut(0xfffff5ee)
	val SelectBlue = immut(0xff4394ff)
	val Sienna = immut(0xffa0522d)
	val SkyBlue = immut(0xff87ceeb)
	val SlateBlue = immut(0xff6a5acd)
	val SlateGray = immut(0xff708090)
	val Snow = immut(0xfffffafa)
	val SpringGreen = immut(0xff00f7f)
	val SteelBlue = immut(0xff4682b4)
	val Tan = immut(0xffd2b48c)
	val Thistle = immut(0xffd8bfd8)
	val Tomato = immut(0xffff6347)
	val Turquoise = immut(0xff40e0d0)
	val UmmGold = immut(0xffffcc33)
	val UmmMaroon = immut(0xff660000)
	val Violet = immut(0xffee82ee)
	val VioletRed = immut(0xffd02090)
	val Wheat = immut(0xfff5deb3)
	val White = immut(0xffffffff)
	val WhiteSmoke = immut(0xfff5f5f5)
	val Yellow = immut(0xffffff00)
	val YellowGreen = immut(0xff9acd32)

	val Clear = immut(0x00fffff)

  def mut(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 1.0): mutable.MutableColor = {
    new mutable.MutableColor(red, green, blue, alpha)
  }

  def mut(value: Long): mutable.MutableColor = {
    val alpha = (value >> 24 & 0xff) / 255.0
		val red = (value >> 16 & 0xff) / 255.0
		val green = (value >> 8 & 0xff) / 255.0
		val blue = (value >> 0 & 0xff) / 255.0
    mut(red, green, blue, alpha)
  }

  def mut(red: Int, green: Int, blue: Int, alpha: Int): mutable.MutableColor = {
    mut(red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0)
  }

  def mut(hex: String): mutable.MutableColor = {
    val (red, green, blue, alpha) = convertHex(hex)
    new mutable.MutableColor(red, green, blue, alpha)
  }

  def immut(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 1.0): immutable.ImmutableColor = {
    new immutable.ImmutableColor(red, green, blue, alpha)
  }

  def immut(value: Long): immutable.ImmutableColor = {
    val alpha = (value >> 24 & 0xff) / 255.0
		val red = (value >> 16 & 0xff) / 255.0
		val green = (value >> 8 & 0xff) / 255.0
		val blue = (value >> 0 & 0xff) / 255.0
    immut(red, green, blue, alpha)
  }

  def immut(red: Int, green: Int, blue: Int, alpha: Int): immutable.ImmutableColor = {
    immut(red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0)
  }

  def immut(hex: String): immutable.ImmutableColor = {
    val (red, green, blue, alpha) = convertHex(hex)
    new immutable.ImmutableColor(red, green, blue, alpha)
  }

  private def convertHex(hex: String): (Double, Double, Double, Double) = {
    if (hex.startsWith("#")) {
      convertHex(hex.substring(1))
    } else if ((hex.length == 3) || (hex.length == 4)) {		// Convert 3-digit / 4-digit to 6-digit / 8-digit
      val b = new StringBuilder()
      for (i <- 0 until hex.length) {
        b.append(hex.charAt(i))
        b.append(hex.charAt(i))
      }
      convertHex(b.toString())
    } else if (hex.length >= 6) {
      val red = (fromHex(hex.charAt(0)) * 16) + fromHex(hex.charAt(1))
      val green = (fromHex(hex.charAt(2)) * 16) + fromHex(hex.charAt(3))
      val blue = (fromHex(hex.charAt(4)) * 16) + fromHex(hex.charAt(5))
      var alpha = 255
      if (hex.length == 8) {
        alpha = (fromHex(hex.charAt(6)) * 16) + fromHex(hex.charAt(7))
      }
      (red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0)
    } else {
      throw new RuntimeException("Unable to parse " + hex + " to Color")
    }
  }

  private def fromHex(c: Char) = if (c.isDigit) {
		c.toString.toInt
	} else {
		c.toLower match {
			case 'a' => 10
			case 'b' => 11
			case 'c' => 12
			case 'd' => 13
			case 'e' => 14
			case 'f' => 15
			case _ => throw new RuntimeException("Unable to parse character to hex: " + c)
		}
	}
}