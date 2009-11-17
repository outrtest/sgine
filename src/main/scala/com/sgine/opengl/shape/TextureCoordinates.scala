package com.sgine.opengl.shape

import com.sgine.opengl._
import com.sgine.opengl.point._

class TextureCoordinates(val length:Int) {
	lazy val coords = new Array[Point2](length);
	
	def apply(index:Int) = {
		coords(index);
	}
	
	def update(index:Int, point:Point2) = {
		coords(index) = point;
	}
}
