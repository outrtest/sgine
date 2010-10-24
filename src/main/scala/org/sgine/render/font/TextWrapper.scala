package org.sgine.render.font

trait TextWrapper {
	def apply(text: String, wrapWidth: Double, font: Font, kern: Boolean): List[String]
}

object NoWrap extends TextWrapper {
	def apply(text: String, wrapWidth: Double, font: Font, kern: Boolean) = {
		val t = text.replaceAll("\r\n", " ").replaceAll("\r", " ")
		List(t)
	}
}

object WordWrap extends TextWrapper {
	def apply(text: String, wrapWidth: Double, font: Font, kern: Boolean) = {
		var list: List[String] = Nil
		var lineWidth = 0.0
		val words = getWords(text)
		val b = new StringBuilder()
		for (word <- words) {
			val wordWidth = font.measureWidth(word, kern)
			val includeWord = lineWidth + wordWidth <= wrapWidth || b.length == 0
			if (!includeWord) {
				list = b.toString :: list
				b.clear()
				lineWidth = 0.0
			}
			b.append(word)
			lineWidth += wordWidth
			if (word.endsWith("\n")) {
				lineWidth = Int.MaxValue
			}
		}
		if (b.length > 0) {
			list = b.toString :: list
		}
		list.reverse
	}
	
	def getWords(text: String) = {
		val t = text.replaceAll("\r\n", "\n").replaceAll("\r", "\n")
		var list: List[String] = Nil
		val b = new StringBuilder()
		for (c <- t) {
			val split = c match {
				case ' ' => true
				case '\n' => true
				case _ => false
			}
			b.append(c)
			if (split) {
				list = b.toString :: list
				b.clear()
			}
		}
		if (b.length > 0) {
			list = b.toString :: list
		}
		
		list.reverse
	}
}