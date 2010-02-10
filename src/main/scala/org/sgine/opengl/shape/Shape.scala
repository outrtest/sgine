package org.sgine.opengl.shape

import org.sgine.opengl._
import org.sgine.math._;
import org.sgine.math.mutable.{Vector3 => MutableVector3}
import org.sgine.util._;

import java.awt.image._;
import java.net._
import javax.imageio._;

import org.lwjgl.opengl.GL11._;

class Shape (val shapeType:Int, val points: Vector3*) extends Function1[Double, Unit] {
	lazy val vertices = points;
	val texture = new Texture();
	val textureCoordinates = new TextureCoordinates(vertices.length);
	
	def apply(time:Double) = {
		texture.draw();
		glBegin(shapeType);
		for (i <- 0 until vertices.size) {
			drawVertex(vertices(i), i)
		}
		glEnd();
	}
	
	def apply(image: BufferedImage, width: Int = -1, height: Int = -1) = {
		var w = width
		var h = height
		if ((width == -1) && (height == -1)) {			// Use image width and height
			w = image.getWidth
			h = image.getHeight
		} else if (height == -1) {						// Constrain to width
			val conversion = w.toDouble / image.getWidth.toDouble
			h = Math.round(image.getHeight.toDouble * conversion).toInt
		} else if (width == -1) {						// Constrain to height
			val conversion = h.toDouble / image.getHeight.toDouble
			w = Math.round(image.getWidth.toDouble * conversion).toInt
		}
		val xb = w / 2.0;
		val yb = h / 2.0;
		
		points(0) match { case p: MutableVector3 => p.set(-xb, -yb, 0.0) }
		points(1) match { case p: MutableVector3 => p.set(xb, -yb, 0.0) }
		points(2) match { case p: MutableVector3 => p.set(xb, yb, 0.0) }
		points(3) match { case p: MutableVector3 => p.set(-xb, yb, 0.0) }
		
		texture(image, 0, 0, image.getWidth, image.getHeight, true);
	}
	
	private def drawVertex(vertex:Vector3, index:Int) = {
		val coords = textureCoordinates(index);
		if (coords != null) {			// Texture coordinates optional
			glTexCoord2d(coords.x, coords.y);
		}
		glVertex3d(vertex.x, vertex.y, vertex.z);
	}
}

object Shape {
	def apply(shapeType:Int, vertices:Vector3*) = new Shape(shapeType, vertices: _*);
	
	def apply(image:BufferedImage):Shape = {
		val xb = image.getWidth / 2.0;
		val yb = image.getHeight / 2.0;
		val s = Shape(
				GL_TRIANGLES,
				Vector3(-xb, -yb, 0.0),		// Bottom-Left
				Vector3(xb, -yb, 0.0),		// Bottom-Right
				Vector3(xb, yb, 0.0),		// Top-Right
				Vector3(-xb, yb, 0.0),		// Top-Left
				Vector3(-xb, -yb, 0.0),		// Bottom-Left
				Vector3(xb, yb, 0.0)		// Top-Right
			);
		s.textureCoordinates(0) = Vector2.UnitY;
		s.textureCoordinates(1) = Vector2.Ones;
		s.textureCoordinates(2) = Vector2.UnitX;
		s.textureCoordinates(3) = Vector2.Zero;
		s.textureCoordinates(4) = Vector2.UnitY;
		s.textureCoordinates(5) = Vector2.UnitX;
		
		s.texture(image, 0, 0, image.getWidth, image.getHeight, true);
		
		s;
	}
}

object GeneralReusableGraphic extends ReusableGraphic;
