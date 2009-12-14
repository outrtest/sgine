package org.sgine.opengl

class GLApplet extends java.applet.Applet with GLContainer {
	val awtContainer = this
	
	override def begin() = {
		containerWidth := getWidth
		containerHeight := getHeight
		
		super.begin()
	}
}