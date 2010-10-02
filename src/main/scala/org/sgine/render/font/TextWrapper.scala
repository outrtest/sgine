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
			val word = b.length match {
				case 0 => w
				case _ => " " + w
			}
			val wordWidth = font.measureWidth(word, kern)
			if ((lineWidth + wordWidth <= wrapWidth) || (b.length == 0)) {
				b.append(word)
				lineWidth += wordWidth
			} else {
				list = b.toString :: list
				b.clear()
				val trimmed = word.trim
				b.append(trimmed)
				lineWidth = font.measureWidth(trimmed, kern)
			}
		}
		if (b.length > 0) {
			list = b.toString :: list
		}
		list.reverse
	}
}