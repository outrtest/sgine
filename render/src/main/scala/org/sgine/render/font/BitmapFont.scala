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

package org.sgine.render.font

import org.sgine.resource.Resource

import org.sgine.XMLUtil._
import xml.{Node, XML}
import org.sgine.render._
import collection.mutable.ListBuffer

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class BitmapFont(texture: Texture) extends TextureMap[Int, BitmapFontChar](texture) {
  private[font] var _face: String = null
	private[font] var _style: Int = BitmapFont.Plain
	private[font] var _size: Double = 0.0
	private[font] var _bold: Int = 0
	private[font] var _italic: Int = 0
	private[font] var _charset: String = ""
	private[font] var _unicode: Int = 0
	private[font] var _stretchH: Int = 0
	private[font] var _smooth: Int = 0
	private[font] var _aa: Int = 0
	private[font] var _padding: Array[Int] = new Array[Int](4)
	private[font] var _spacing: Array[Int] = new Array[Int](2)
	private[font] var _lineHeight: Int = 0
	private[font] var _base: Int = 0
	private[font] var _scaleW: Int = 0
	private[font] var _scaleH: Int = 0
	private[font] var _pages: Int = 0
	private[font] var _packed: Int = 0

	def face = _face
	def style = _style
	def size = _size
	def bold = _bold
	def italic = _italic
	def charset = _charset
	def unicode = _unicode
	def stretchH = _stretchH
	def smooth = _smooth
	def aa = _aa
	def padding = _padding
	def spacing = _spacing
	def lineHeight = _lineHeight
	def base = _base
	def scaleW = _scaleW
	def scaleH = _scaleH

  def apply(s: String, shape: Shape, kern: Boolean = true): Unit = {
    var vertices = new ListBuffer[Double]
    var textureCoords = new ListBuffer[Double]
    val width = measure(s, kern)
    val height = lineHeight
    var x = -width / 2.0
    var pfc: BitmapFontChar = null
    s.foreach(c => {
      val fc = apply(c)
      for (index <- 0 until fc.vertices.length by 3) {
        vertices.append(fc.vertices(index) + x)
        vertices.append(fc.vertices(index + 1))
        vertices.append(fc.vertices(index + 2))
      }
//      vertices.appendAll(fc.vertices)
      textureCoords.appendAll(fc.textureCoords)

      x += fc.xAdvance
      if (kern && pfc != null) x += fc.kerning(pfc.code)
      pfc = fc
    })
    shape.updateVertices(vertices)
    shape.updateTexture(texture, textureCoords)
  }

  def measure(s: String, kern: Boolean = true) = measureRec(s, -1, 0.0, kern)

  private def measureRec(s: String, previous: Int, width: Double, kern: Boolean): Double = {
    if (!s.isEmpty) {
      val c = s.head
      val fc = apply(c)
      val kerning = if (kern) fc.kerning(previous) else 0.0
      measureRec(s.tail, fc.code, width + fc.xAdvance + kerning, kern)
    } else {
      width
    }
  }
}

object BitmapFont {
  val Plain = 0
	val Bold = 1
	val Italic = 2

  def apply(fnt: Resource) = {
    // Load XML
    val xml = XML.load(fnt.url)

    // Reference main elements
    val info = xml \! "info"
		val common = xml \! "common"
		val pages = xml \! "pages" \ "page"
    if (pages.length != 1) {
			throw new RuntimeException("Multiple paged fonts are currently not supported!")
		}
		val page = pages.head
		val chars = xml \! "chars"
		val kernings = xml \! "kernings"

		// Load texture
		val filename = page \@ "file"
		val file = fnt.parent.child(filename)
    val texture = TextureUtils(file)

    // Create BitmapFont and load info
    val font = new BitmapFont(texture)
    loadInfo(font, info)
    loadCommon(font, common)

    // Populate BitmapFontChars
    (chars \ "char").foreach(char => loadChar(texture, font, char))

    // Populate kerning data
    (kernings \ "kerning").foreach(kerning => loadKerning(font, kerning))

    font
  }

  private def loadInfo(font: BitmapFont, info: Node) = {
		font._face = info \@ "face"
		if (font.face.indexOf(',') != -1) {
			val split = font.face.indexOf(',')
			val face = font.face.substring(0, split)
			val style = font.face.substring(split + 1)
			font._face = face
			font._style = style match {
				case "Normal" => Plain
				case _ => throw new RuntimeException("Unknown style: " + style)
			}
		}

		font._size = (info \@ "size").toInt
		font._bold = (info \@ "bold").toInt
		font._italic = (info \@ "italic").toInt
		font._charset = info \@ "charset"
		font._unicode = (info \@ "unicode").toInt
		font._stretchH = (info \@ "stretchH").toInt
		font._smooth = (info \@ "smooth").toInt
		font._aa = (info \@ "aa").toInt

		val p = (info \@ "padding").split(",")
		font._padding(0) = p(0).toInt
		font._padding(1) = p(1).toInt
		font._padding(2) = p(2).toInt
		font._padding(3) = p(3).toInt

		val s = (info \@ "spacing").split(",")
		font._spacing(0) = p(0).toInt
		font._spacing(1) = p(1).toInt
	}

	private def loadCommon(font: BitmapFont, common: Node) = {
		font._lineHeight = (common \@ "lineHeight").toInt
		font._base = (common \@ "base").toInt
		font._scaleW = (common \@ "scaleW").toInt
		font._scaleH = (common \@ "scaleH").toInt
		font._pages = (common \@ "pages").toInt
		font._packed = (common \@ "packed").toInt
	}

	private def loadChar(texture: Texture, font: BitmapFont, char: Node) = {
		val id = (char \@ "id").toInt
		val x = (char \@ "x").toInt
		val y = (char \@ "y").toInt
		val width = (char \@ "width").toInt
		val height = (char \@ "height").toInt
    val vertices = Vertices.rectLeft(width, height)
    val textureCoords = Vertices.rectCoords(x, y, width, height, texture)
    val fontChar = new BitmapFontChar(vertices, textureCoords)
		fontChar._font = font
		fontChar._code = id
		fontChar._x = x
		fontChar._y = y
		fontChar._width = width
		fontChar._height = height
		fontChar._xOffset = (char \@ "xoffset").toDouble
		fontChar._yOffset = (char \@ "yoffset").toDouble
		fontChar._xAdvance = (char \@ "xadvance").toDouble
    font.map += id -> fontChar
	}

	private def loadKerning(font: BitmapFont, kerning: Node) = {
		val first = (kerning \@ "first").toInt
		val second = (kerning \@ "second").toInt
		val amount = (kerning \@ "amount").toInt
    val next = font(second)
    next._kernings += first -> amount.toDouble
	}
}