package org.sgine.visual.awt

import org.sgine.property.BindingProperty
import org.sgine.property.DelegateProperty
import org.sgine.visual.Window

class AWTFrame(window: Window) extends java.awt.Frame {
	val frameTitle = new DelegateProperty[String](getTitle, setTitle) with BindingProperty[String]
	frameTitle.bind(window.title)
	
	setSize(1024, 768)
	setResizable(false)
	
	addWindowListener(new java.awt.event.WindowAdapter() {
		override def windowClosing(e: java.awt.event.WindowEvent): Unit = {
			window.stop()
		}
	})
}