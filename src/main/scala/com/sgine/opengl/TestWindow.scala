package com.sgine.opengl

import com.sgine.opengl.shape._;
import point._;

import GLContext._;
import generated.OpenGL2._;

object TestWindow {
	def main(args:Array[String]):Unit = {
		val w = new Window("Test Window", 800, 600);
		val s = Shape(
			GL_QUADS,
			(0.0, 0.0),
			(1.0, 0.0),
			(1.0, 1.0),
			(0.0, 1.0)
		)
		s.textureCoordinates(0) = (0.0, 1.0);
		s.textureCoordinates(1) = (1.0, 1.0);
		s.textureCoordinates(2) = (1.0, 0.0);
		s.textureCoordinates(3) = (0.0, 0.0);
		w.displayables.add(s);
		w.start();
	}
}
