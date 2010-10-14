package org.sgine.render.font

import java.awt.{Font => AWTFont}

import org.sgine.core.HorizontalAlignment

import org.sgine.render.shape.MutableShape

trait Font {
	def face: String
	def size: Double
	def bold: Int
	def italic: Int
	def lineHeight: Int
	def style: Int
	
	def apply(c: Int): Option[FontChar]
	
	def apply(shape: MutableShape, text: String, kern: Boolean = true, wrapWidth: Double = -1.0, wrapMethod: TextWrapper = WordWrap, textAlignment: HorizontalAlignment = HorizontalAlignment.Center): Seq[RenderedLine]
	
	def drawString(s: String, kern: Boolean = true): Unit
	
	def measureWidth(s: String, kern: Boolean = true): Double
	
	def derive(size: Double): Font
}

object Font {
	val Plain = AWTFont.PLAIN
	val Bold = AWTFont.BOLD
	val Italic = AWTFont.ITALIC
}