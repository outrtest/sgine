package com.sgine.opengl.shape

import com.sgine.opengl._
import com.sgine.opengl.point._;
import com.sgine.util._;

import java.awt.image._;
import javax.imageio._;

import com.sgine.opengl.GLContext._;
import com.sgine.opengl.generated.OpenGL2._;

class Shape(val shapeType:Int, val points:Point3*) extends Function1[Double, Unit] {
	lazy val vertices = points zipWithIndex;
	val texture = new Texture();
	val textureCoordinates = new TextureCoordinates(vertices.length);
	
	def apply(time:Double) = {
		texture.draw();
		glBegin(shapeType);
		vertices.foreach(Function.tupled(drawVertex));
		glEnd();
	}
	
	private def drawVertex(vertex:Point3, index:Int) = {
		val coords = textureCoordinates(index);
		glTexCoord2d(coords.x, coords.y);
		glVertex3d(vertex.x, vertex.y, vertex.z);
	}
}

object Shape {
	def apply(shapeType:Int, vertices:Point3*) = new Shape(shapeType, vertices:_*);
	
	def apply(image:BufferedImage):Shape = {
		val xb = image.getWidth / 2.0;
		val yb = image.getHeight / 2.0;
		val s = Shape(
				GL_QUADS,
				(-xb, -yb),
				(xb, -yb),
				(xb, yb),
				(-xb, yb)
			);
		s.textureCoordinates(0) = (0.0, 1.0);
		s.textureCoordinates(1) = (1.0, 1.0);
		s.textureCoordinates(2) = (1.0, 0.0);
		s.textureCoordinates(3) = (0.0, 0.0);
		
		if (s.texture.isValidImage(image)) {
			s.texture(image, 0, 0, image.getWidth, image.getHeight, true);
		} else {
			val rg = GeneralReusableGraphic;
			val g = rg(image.getWidth, image.getHeight, -1);
			try {
				g.drawImage(image, 0, 0, image.getWidth, image.getHeight, null);
				g.dispose();
				
				s.texture(rg(), 0, 0, image.getWidth, image.getHeight, true);
			} finally {
				rg.release();
			}
		}
		
		s;
	}
}

object GeneralReusableGraphic extends ReusableGraphic;