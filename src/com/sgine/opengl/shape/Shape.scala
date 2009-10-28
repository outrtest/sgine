package com.sgine.opengl.shape

import com.sgine.opengl._
import point.Point3D;

import com.sgine.opengl.GLContext._;
import com.sgine.opengl.generated.OpenGL2._;

class Shape(val shapeType:Int, val points:Point3D*) extends Function1[Double, Unit] {
	lazy val vertices = points zipWithIndex;
	val texture = null;
	val textureCoordinates = new TextureCoordinates(vertices.length);
	
	def apply(time:Double) = {
		glBegin(shapeType);
		vertices.foreach(Function.tupled(drawVertex));
		glEnd();
	}
	
	private def drawVertex(vertex:Point3D, index:Int) = {
		val coords = textureCoordinates(index);
		glTexCoord2d(coords.x, coords.y);
		glVertex3d(vertex.x, vertex.y, vertex.z);
	}
}

object Shape {
	def apply(shapeType:Int, vertices:Point3D*) = {
		println(vertices.length);
		new Shape(shapeType, vertices:_*);
	}
}