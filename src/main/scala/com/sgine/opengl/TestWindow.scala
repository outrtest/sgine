package com.sgine.opengl

import javax.imageio._;

import com.sgine.util._;
import shape._;
import point._;
import state._;

import GLContext._;
import generated.OpenGL2._;

object TestWindow {
	def main(args:Array[String]):Unit = {
		val w = new Window("Test Window", 800, 600);
		
		w.displayables.add(TranslateState(0.0, 0.0, -1000.0));
		
		val s = Shape(ImageIO.read(classOf[Window].getClassLoader().getResource("resource/puppies.jpg")));
		w.displayables.add(s);
		
		w.start();
	}
}
