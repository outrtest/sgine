package com.sgine.opengl.shape

import com.sgine.opengl._;

import com.sgine.opengl.GLContext._;
import com.sgine.opengl.generated.OpenGL2._;

class Shape(val shapeType:Int, val vertices:Point3D*) extends Function1[Double, Unit] {
	def apply(time:Double) = {
		glBegin(shapeType);
		vertices.foreach(drawVertex);
		glEnd();
	}
	
	private def drawVertex(vertex:Point3D) = {
		glVertex3d(vertex.x, vertex.y, vertex.z);
	}
}

object Shape {
	def apply(shapeType:Int)(vertices:Point3D*) = {
		println(vertices.length);
		new Shape(shapeType, vertices:_*);
	}
}