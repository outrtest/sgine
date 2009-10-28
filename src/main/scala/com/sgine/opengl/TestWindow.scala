package com.sgine.opengl

import javax.imageio._;

import com.sgine.util._;
import shape._;
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
		
		val awtImage = ImageIO.read(classOf[Window].getClassLoader().getResource("resource/puppies.jpg"));
		val rg = new ReusableGraphic();
		val g = rg(awtImage.getWidth, awtImage.getHeight);
		g.drawImage(awtImage, 0, 0, awtImage.getWidth, awtImage.getHeight, null);
		g.dispose();
		s.texture(rg(), 0, 0, awtImage.getWidth, awtImage.getHeight, true);
		
		w.displayables.add(s);
		w.start();
	}
}
