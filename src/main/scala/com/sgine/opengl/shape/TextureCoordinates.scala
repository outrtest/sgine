package com.sgine.opengl.shape

import com.sgine.opengl._
import point.Point2d;

class TextureCoordinates(val length:Int) {
	lazy val coords = new Array[Point2d](length);
	
	def apply(index:Int) = {
		coords(index);
	}
	
	def update(index:Int, point:Point2d) = {
		coords(index) = point;
	}
}
