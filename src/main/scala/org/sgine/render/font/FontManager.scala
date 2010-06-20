package org.sgine.render.font

// TODO: add support for dynamic font generation
object FontManager {
	private var fonts = Map.empty[String, Font]
	
	def apply(name: String): Font = {
		synchronized {
			if (!fonts.contains(name)) {
				val font = BitmapFont(name)
				fonts += name -> font
			}
		}
		fonts(name)
	}
}