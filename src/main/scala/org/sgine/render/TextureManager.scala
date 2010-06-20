package org.sgine.render

import java.net.URL

import javax.imageio.ImageIO

import org.sgine.core.Resource

object TextureManager {
	private var map = Map.empty[URL, Texture]
	
	def apply(resource: Resource) = {
		synchronized {
			if (map.contains(resource.url)) {
				map(resource.url)
			} else {
				val t = TextureUtil(ImageIO.read(resource.url))
				map += resource.url -> t
				t
			}
		}
	}
}