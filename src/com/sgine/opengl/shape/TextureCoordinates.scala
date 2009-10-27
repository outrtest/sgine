package com.sgine.opengl.shape

class TextureCoordinates(val length:Int) {
	lazy val coords = new Array[Double](length);
	
	def apply(index:Int) = {
		coords(index);
	}
	
	def update(index:Int, value:Double) = {
		coords(index) = value;
	}
}
