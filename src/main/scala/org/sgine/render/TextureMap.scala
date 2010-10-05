package org.sgine.render

import org.sgine.render.shape.Quad

class TextureMap[T, I] protected(val texture: Texture)(implicit creator: Function1[Quad, I]) {
	private var map = Map.empty[T, I]
	
	def create(name: T, x: Double, y: Double, width: Double, height: Double, scale: Double = 1.0) = {
		val quad = Quad.subtexture(texture, x, y, width, height, scale)
		val instance = creator(quad)
		synchronized {				// Make sure map doesn't get overwritten
			map += name -> instance
		}
		
		instance
	}
	
	def apply(name: T) = map.get(name)
	
	def instances = map.values
	
	def length = map.size
}

object TextureMap {
	implicit val quadMap = (quad: Quad) => quad
	
	def apply[T, I](texture: Texture)(implicit creator: Function1[Quad, I]) = new TextureMap[T, I](texture)(creator)
}