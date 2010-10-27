package org.sgine.render

import org.sgine.core.ProcessingMode
import org.sgine.core.Resource

import org.sgine.event.EventHandler

import org.sgine.input.Key
import org.sgine.input.Keyboard
import org.sgine.input.event.KeyPressEvent

import org.sgine.log._

import org.sgine.render.font.FontManager

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import org.sgine.ui.FPSLabel
import org.sgine.ui.Image

trait Debug extends Display {
	val debugContainer = new GeneralNodeContainer() with ResolutionNode
	val fps = new FPSLabel()
	val grid = new Image()
	
	abstract override def init() = {
		super.init()
		
		renderer.verticalSync := false
		
		debugContainer.setResolution(1024, 768)
		scene += debugContainer
		
		fps.font := FontManager("Digital-7", 36.0)
		fps.color := org.sgine.core.Color.DarkGreen
		fps.location.x := -520.0
		fps.location.x.align := org.sgine.core.HorizontalAlignment.Left
		fps.location.y := 380.0
		fps.location.y.align := org.sgine.core.VerticalAlignment.Top
		fps.location.z := 1.0
		debugContainer += fps
		
		grid.source := Resource("grid.png")
		grid.location.z := 400.0
		grid.scale(0.5)
		grid.visible := false
		debugContainer += grid
		
		Keyboard.listeners += EventHandler(handleKey, ProcessingMode.Blocking, worker = renderer)
	}
	
	private def handleKey(evt: KeyPressEvent) = {
		evt.key.toUpperCase match {
			case Key.Escape => Renderer().shutdown()				// Shutdown
			case Key.F1 => fps.visible := !fps.visible(); info("FPS %1s", args = List(if (Renderer().fullscreen()) "enabled" else "disabled"))
			case Key.F2 => Renderer().lighting := !Renderer().lighting(); info("Lighting turned " + (if (Renderer().lighting()) "on" else "off"))
			case Key.F3 => {
				if (Renderer().polygonFront() == PolygonMode.Fill) {
					Renderer().polygonFront := PolygonMode.Line
					Renderer().polygonBack := PolygonMode.Line
					info("Switched to polygon line")
				} else {
					Renderer().polygonFront := PolygonMode.Fill
					Renderer().polygonBack := PolygonMode.Fill
					info("Switched to polygon fill")
				}
			}
			case Key.F4 => grid.visible := !grid.visible(); info("Grid %1s", args = List(if (grid.visible()) "enabled" else "disabled"))
			case Key.F5 => Renderer().verticalSync := !Renderer().verticalSync(); info("Vertical Sync %1s", args = List(if (Renderer().verticalSync()) "enabled" else "disabled"))
			case Key.F12 => Renderer().fullscreen := !Renderer().fullscreen(); info("Fullscreen %1s", args = List(if (Renderer().fullscreen()) "enabled" else "disabled"))
			case _ =>
		}
	}
}