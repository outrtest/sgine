package org.sgine.opengl

import javax.imageio._

import org.sgine.util._
import shape._
import org.sgine.math._
import state._

object TestWindow {
	def main(args:Array[String]):Unit = {
		// Create our window
		val w = GLWindow("Test Window", 1024, 768);
		
		// Translate back so we can see
		w.displayables.add(TranslateState(0.0, 0.0, -1000.0));
		
		// Create a quad shape with a texture and add it
		val s = Shape(ImageIO.read(classOf[GLWindow].getClassLoader().getResource("resource/puppies.jpg")));
		w.displayables.add(s);
		
		// Add an FPS counter to see how fast we're going
		w.displayables.add(FPS());
		
		// Start / Display the window
		w.begin();
	}	
}