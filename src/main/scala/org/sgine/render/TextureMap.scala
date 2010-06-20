package org.sgine.render

class TextureMap[T] protected(val texture: Texture) {
	private var map = Map.empty[Any, RenderImage]
	
	def create(name: T, x: Double, y: Double, width: Double, height: Double) = {
		val image = createImage()
		image.texture = texture
		image.x = x
		image.y = y
		image.width = width
		image.height = height
		
		synchronized {				// Make sure map doesn't get overwritten
			map += name -> image
		}
		
		image
	}
	
	protected def createImage() = RenderImage()
	
	def apply(name: T) = map(name)
	
	def length = map.size
}

object TextureMap {
	def apply(texture: Texture) = new TextureMap(texture)
}