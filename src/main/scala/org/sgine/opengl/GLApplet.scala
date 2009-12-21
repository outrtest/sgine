package org.sgine.opengl

class GLApplet extends java.applet.Applet with GLContainer {
	val awtContainer = this
	
	override final def init = {
		super.init()
		
		begin()
	}
	
	override def begin() = {
		containerWidth := getWidth
		containerHeight := getHeight
		
		super.begin()
	}
}