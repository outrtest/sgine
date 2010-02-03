package org.sgine.opengl

class GLApplet extends java.applet.Applet with GLContainer {
	override final def init = {
		super.init()
		
		begin()
	}
	
	override def begin(awtContainer: java.awt.Container = this) = {
		containerWidth := getWidth
		containerHeight := getHeight
		
		super.begin(awtContainer)
	}
}