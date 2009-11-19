package com.sgine.opengl

import javax.imageio._

import com.sgine.util._
import shape._
import com.sgine.math._
import state._

import GLContext._
import generated.OpenGL2._

object TestWindow {
	def main(args:Array[String]):Unit = {
		// Create our window
		val w = new Window("Test Window", 800, 600);
		
		// Translate back so we can see
		w.displayables.add(TranslateState(0.0, 0.0, -1000.0));
		
		// Create a quad shape with a texture and add it
		val s = Shape(ImageIO.read(classOf[Window].getClassLoader().getResource("resource/puppies.jpg")));
		w.displayables.add(s);
		
		// Add an FPS counter to see how fast we're going
		w.displayables.add(FPS());
		
		// Start / Display the window
		w.start();
	}	
}