package com.sgine.opengl.shape

import com.sgine.opengl.GLContext._;
import com.sgine.opengl.generated.OpenGL2._;

import java.awt.image.BufferedImage;

class Texture {
	private lazy val id = generateId();
	
	private def generateId() = {
		val tmp = new Array[Int](1);
		glGenTextures(1, tmp, 0);
		tmp(0);
	}
	
	def update(image:BufferedImage, x:Int, y:Int, width:Int, height:Int, mipmap:Boolean) = {
		
	}
	
	def isValidImage(image:BufferedImage) = {
//		if (image.getColorModel() == Reus)
	}
}