package org.sgine.opengl

import javax.imageio._

import org.sgine.util._
import shape._
import org.sgine.math._
import state._

class TestApplet extends GLApplet {
	override def begin(awtContainer: java.awt.Container = this) = {
		// Translate back so we can see
		displayables.add(TranslateState(0.0, 0.0, -1000.0));
		
		// Create a quad shape with a texture and add it
		val s = Shape(ImageIO.read(classOf[GLWindow].getClassLoader().getResource("resource/puppies.jpg")));
		displayables.add(s);
		
		// Add an FPS counter to see how fast we're going
		displayables.add(FPS());
		
		super.begin(awtContainer)
	}
}