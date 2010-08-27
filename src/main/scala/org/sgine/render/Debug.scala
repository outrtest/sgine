package org.sgine.render

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.Key
import org.sgine.input.Keyboard
import org.sgine.input.event.KeyPressEvent

import org.sgine.log._

import org.sgine.render.font.FontManager

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import org.sgine.ui.FPSLabel

trait Debug extends Display {
	val debugContainer = new GeneralNodeContainer() with ResolutionNode
	val fps = new FPSLabel()
	
	abstract override def init() = {
		super.init()
		
		renderer.verticalSync := false
		
		debugContainer.setResolution(1600, 1200)
		scene += debugContainer
		
		fps.font := FontManager("lcd")
		fps.location.x := -795.0
		fps.location.x.align := org.sgine.core.HorizontalAlignment.Left
		fps.location.y := 595.0
		fps.location.y.align := org.sgine.core.VerticalAlignment.Top
		fps.location.z := 1.0
		debugContainer += fps
		
		Keyboard.listeners += EventHandler(handleKey, ProcessingMode.Blocking, worker = renderer)
	}
	
	private def handleKey(evt: KeyPressEvent) = {
		if (evt.key == Key.Escape) {					// Shutdown
			Renderer().shutdown()
		} else if (evt.keyChar.toLower == 'l') {		// Toggle lighting
			Renderer().lighting := !Renderer().lighting()
			info("Lighting turned " + (if (Renderer().lighting()) "on" else "off"))
		} else if (evt.keyChar.toLower == 'f') {		// Toggle fps display
			fps.visible := !fps.visible()
			info("Size: %1sx%2s - %3s", args = List(fps.dimension.width(), fps.dimension.height(), fps.bounding()))
		}
	}
}