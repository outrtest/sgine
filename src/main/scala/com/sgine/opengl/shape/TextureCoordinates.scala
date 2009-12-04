package com.sgine.opengl.shape

import com.sgine.opengl._
import com.sgine.math._

class TextureCoordinates(val length:Int) {
	lazy val coords = new Array[Vector2](length);
	
	def apply(index:Int) = {
		coords(index);
	}
	
	def update(index:Int, point:Vector2) = {
		coords(index) = point;
	}
}
