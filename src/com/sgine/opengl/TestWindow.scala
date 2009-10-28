package com.sgine.opengl

import com.sgine.opengl.shape._;
import point.Point3D;
import point.Point2D;

import GLContext._;
import generated.OpenGL2._;

object TestWindow {
	def main(args:Array[String]):Unit = {
		val w = new Window("Test Window", 800, 600);
		val s = Shape(
			GL_QUADS,
			Point3D(),
			Point3D(1.0, 0.0),
			Point3D(1.0, 1.0),
			Point3D(0.0, 1.0)
		)
		s.textureCoordinates(0) = Point2D(0.0, 1.0);
		s.textureCoordinates(1) = Point2D(1.0, 1.0);
		s.textureCoordinates(2) = Point2D(1.0, 0.0);
		s.textureCoordinates(3) = Point2D(0.0, 0.0);
		w.displayables.add(s);
		w.start();
	}
}
