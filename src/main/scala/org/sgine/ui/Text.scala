package org.sgine.ui

import org.sgine.core.ProcessingMode

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty

import org.sgine.render.font.Font
import org.sgine.render.font.FontManager

import org.lwjgl.opengl.GL11._

class Text extends Component {
	val font = new AdvancedProperty[Font](FontManager("Arial32"), this)
	val text = new AdvancedProperty[String]("", this)
	
	Listenable.listenTo(EventHandler(invalidateText, ProcessingMode.Blocking), font, text, size.width, size.height)
	private val revalidateText = new java.util.concurrent.atomic.AtomicBoolean(false)
	
	private var lines: List[String] = Nil
	
	def drawComponent() = {
		if (revalidateText.get) {
			revalidateText.set(false)
			validateText()
		}
		
		lines.foreach(renderLine)
	}
	
	private val renderLine = (line: String) => {
		val width = font().measureWidth(line, true)
		font().drawString(line, true)
		glTranslated(-width / 2.0, -font().lineHeight, 0.0)		// TODO: provide a better way to support this
	}
	
	private def invalidateText(evt: Event) = revalidateText.set(true)
	
	private def validateText() = {
		val text = this.text()
		if (size.width() == 0.0) {
			lines = List(text)
		} else {
			val f = font()
			var lineWidth = 0.0
			val textWidth = size.width()
			var l: List[String] = Nil
			val words = text.split(" ")
			val b = new StringBuilder()
			for (w <- words) {
				val word = b.length match {
					case 0 => w
					case _ => " " + w
				}
				val width = f.measureWidth(word, true)
				if ((lineWidth + width <= textWidth) || (b.length == 0)) {
					b.append(word)
					lineWidth += width
				} else {
					l = b.toString :: l
					b.clear()
					b.append(word)
					lineWidth = width
				}
			}
			if (b.length > 0) {
				l = b.toString :: l
			}
			lines = l.reverse
		}
	}
}