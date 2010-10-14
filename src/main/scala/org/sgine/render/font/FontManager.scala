package org.sgine.render.font

import java.awt.{Font => AWTFont}

import org.sgine.core.Resource

import scala.math._

// TODO: add support for dynamic font generation
object FontManager {
	var nearest = false
	private var fonts: List[Font] = Nil
	
	initializeDefaultFonts()
	
	def apply(awt: AWTFont): Font = apply(awt.getFamily, awt.getSize, awt.getStyle)
	
	def apply(face: String, size: Double, style: Int = Font.Plain): Font = {
		fonts.find(f => f.face == face && f.size == size && f.style == style) match {
			case Some(f) => f
			case None => fonts.foldLeft(null.asInstanceOf[Font])(closestSize(face, size)) match {
				case null => throw new RuntimeException("Unable to find font for face: " + face)
				case f => {
					val font = f.derive(size)
					register(font)
					font
				}
			}
		}
	}
	
	private def closestSize(face: String, size: Double)(f1: Font, f2: Font) = {
		if ((f1 != null) && (f2 != null)) {
			if ((f1.face == face) && (f2.face == face)) {
				val d1 = if (nearest) {
					abs(f1.size - size)
				} else {
					f1.size - size
				}
				val d2 = if (nearest) {
					abs(f2.size - size)
				} else {
					f2.size - size
				}
				if ((d1 > 0.0) && (d2 < 0.0)) {
					f1
				} else if (d2 > 0.0) {
					f2
				} else if (d1 < d2) {
					f1
				} else {
					f2
				}
			} else if (f1.face == face) {
				f1
			} else if (f2.face == face) {
				f2
			} else {
				null
			}
		} else if ((f1 != null) && (f1.face == face)) {
			f1
		} else if ((f2 != null) && (f2.face == face)) {
			f2
		} else {
			null
		}
	}
	
	def register(font: Font) = synchronized {
		fonts = font :: fonts
	}
	
	private def initializeDefaultFonts() = {
		register(BitmapFontLoader.load(Resource("Arial24.fnt")))
		register(BitmapFontLoader.load(Resource("Arial64.fnt")))
		register(BitmapFontLoader.load(Resource("Courier36.fnt")))
		register(BitmapFontLoader.load(Resource("Digital36.fnt")))
	}
}