package org.sgine.ui

import org.sgine.core.HorizontalAlignment
import org.sgine.core.ProcessingMode
import org.sgine.core.VerticalAlignment

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty

import org.sgine.render.font.BitmapFont
import org.sgine.render.font.Font
import org.sgine.render.font.FontManager
import org.sgine.render.font.RenderedCharacter
import org.sgine.render.font.RenderedLine
import org.sgine.render.font.WordWrap

import org.lwjgl.opengl.GL11._

class Text extends ShapeComponent {
	val cull = _cull
	val font = new AdvancedProperty[Font](FontManager("Arial32"), this)
	val text = new AdvancedProperty[String]("", this)
	val kern = new AdvancedProperty[Boolean](true, this)
	val textHorizontalAlignment = new AdvancedProperty[HorizontalAlignment](HorizontalAlignment.Center, this)
	val textVerticalAlignment = new AdvancedProperty[VerticalAlignment](VerticalAlignment.Middle, this)
	
	protected var lines: Seq[RenderedLine] = _
	
	Listenable.listenTo(EventHandler(invalidateText, ProcessingMode.Blocking),
						font,
						text,
						size.width,
						size.height,
						kern,
						textHorizontalAlignment,
						textVerticalAlignment)
	private val revalidateText = new java.util.concurrent.atomic.AtomicBoolean(false)
	
	override def drawComponent() = {
		if (revalidateText.get) {
			revalidateText.set(false)
			validateText()
		}
		
		super.drawComponent()
	}
	
	private def invalidateText(evt: Event) = revalidateText.set(true)
	
	private def validateText() = {
		font()(shape, text(), kern(), size.width(), WordWrap, textVerticalAlignment(), textHorizontalAlignment())
		font() match {
			case bf: BitmapFont => texture = bf.texture
			case _ =>
		}
	}
}