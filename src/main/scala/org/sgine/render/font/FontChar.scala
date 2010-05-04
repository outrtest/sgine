package org.sgine.render.font

trait FontChar {
	def font: Font
	def code: Int
	def xOffset: Double
	def yOffset: Double
	def xAdvance: Double
	
	def kerning(previous: FontChar): Double
	
	def drawChar(previous: FontChar = null, kern: Boolean = true): Unit
	
	def measureCharWidth(previous: FontChar = null, kern: Boolean = true): Double
}