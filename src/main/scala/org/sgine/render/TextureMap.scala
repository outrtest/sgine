package org.sgine.render

class TextureMap protected(val texture: Texture) {
	private var map = Map.empty[String, Image]
	
	def create(name: String, x: Double, y: Double, width: Double, height: Double) = {
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
	
	protected def createImage() = Image()
	
	def apply(name: String) = map(name)
}

object TextureMap {
	def apply(texture: Texture) = new TextureMap(texture)
}