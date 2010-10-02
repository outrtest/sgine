package org.sgine.render.font

trait TextWrapper {
	def apply(text: String, wrapWidth: Double, font: Font, kern: Boolean): Seq[String]
}

object WordWrap extends TextWrapper {
	def apply(text: String, wrapWidth: Double, font: Font, kern: Boolean) = {
		var list: List[String] = Nil
		var lineWidth = 0.0
		val words = text.split(" ")
		val b = new StringBuilder()
		for (w <- words) {
			val word = w + " "
			val wordWidth = font.measureWidth(word, kern)
			if ((lineWidth + wordWidth <= wrapWidth) || (b.length == 0)) {
				b.append(word)
				lineWidth += wordWidth
			} else {
				list = b.toString :: list
				b.clear()
				b.append(word)
				lineWidth = font.measureWidth(word, kern)
			}
		}
		if (b.length > 0) {
			list = b.toString :: list
		}
		list.reverse
	}
}