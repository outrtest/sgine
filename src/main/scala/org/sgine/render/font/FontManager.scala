package org.sgine.render.font

import java.awt.{Font => AWTFont}

import org.sgine.core.Resource

// TODO: add support for dynamic font generation
object FontManager {
	private var fonts: List[Font] = Nil
	
	initializeDefaultFonts()
	
	def apply(awt: AWTFont): Font = apply(awt.getFamily, awt.getSize, awt.getStyle)
	
	def apply(face: String, size: Double, style: Int = Font.Plain): Font = {
		// TODO: add style support
		fonts.find(f => f.face == face && f.size == size).getOrElse(throw new NullPointerException("Unable to find font: " + face + " " + size))
	}
	
	def register(font: Font) = synchronized {
		fonts = font :: fonts
		println("Font: " + font.face + " - " + font.size)
	}
	
	private def initializeDefaultFonts() = {
		register(BitmapFontLoader.load(Resource("Arial24.fnt")))
		register(BitmapFontLoader.load(Resource("Arial64.fnt")))
		register(BitmapFontLoader.load(Resource("Courier36.fnt")))
		register(BitmapFontLoader.load(Resource("Digital36.fnt")))
	}
}