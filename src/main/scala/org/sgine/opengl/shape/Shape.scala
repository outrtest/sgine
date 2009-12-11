package org.sgine.opengl.shape

import org.sgine.opengl._
import org.sgine.math.mutable._;
import org.sgine.util._;

import java.awt.image._;
import java.net._
import javax.imageio._;

import org.lwjgl.opengl.GL11._;

class Shape(val shapeType:Int, val points:Vector3*) extends Function1[Double, Unit] {
	lazy val vertices = points zipWithIndex;
	val texture = new Texture();
	val textureCoordinates = new TextureCoordinates(vertices.length);
	
	def apply(time:Double) = {
		texture.draw();
		glBegin(shapeType);
		vertices.foreach(Function.tupled(drawVertex));
		glEnd();
	}
	
	def apply(image: BufferedImage) = {
		val xb = image.getWidth / 2.0
		val yb = image.getHeight / 2.0
		points(0).set(-xb, -yb, 0.0)
		points(1).set(xb, -yb, 0.0)
		points(2).set(xb, yb, 0.0)
		points(3).set(-xb, yb, 0.0f)
		
		texture(image, 0, 0, image.getWidth, image.getHeight, true);
	}
	
	private def drawVertex(vertex:Vector3, index:Int) = {
		val coords = textureCoordinates(index);
		if (coords != null) {
			glTexCoord2d(coords.x, coords.y);
			glVertex3d(vertex.x, vertex.y, vertex.z);
		}
	}
}

object Shape {
	def apply(shapeType:Int, vertices:Vector3*) = new Shape(shapeType, vertices:_*);
	
	def apply(image:BufferedImage):Shape = {
		val xb = image.getWidth / 2.0;
		val yb = image.getHeight / 2.0;
		val s = Shape(
				GL_QUADS,
				Vector3(-xb, -yb, 0.0),
				Vector3(xb, -yb, 0.0),
				Vector3(xb, yb, 0.0),
				Vector3(-xb, yb, 0.0)
			);
		s.textureCoordinates(0) = Vector2.UnitY;
		s.textureCoordinates(1) = Vector2.Ones;
		s.textureCoordinates(2) = Vector2.UnitX;
		s.textureCoordinates(3) = Vector2.Zero;
		
		s.texture(image, 0, 0, image.getWidth, image.getHeight, true);
		
		s;
	}
}

object GeneralReusableGraphic extends ReusableGraphic;
