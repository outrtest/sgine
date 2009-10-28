package com.sgine.opengl.shape

import com.sgine.opengl._;

class TextureCoordinates(val length:Int) {
	lazy val coords = new Array[Point2D](length);
	
	def apply(index:Int) = {
		coords(index);
	}
	
	def update(index:Int, point:Point2D) = {
		coords(index) = point;
	}
}
