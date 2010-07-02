package org.sgine.render.font

trait Font {
	def face: String
	def size: Int
	def bold: Int
	def italic: Int
	def lineHeight: Int
	
	def apply(c: Int): FontChar
	
	def drawString(s: String, kern: Boolean = true): Unit
	
	def measureWidth(s: String, kern: Boolean = true): Double
}