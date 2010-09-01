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
				// TODO: figure out why PNGDecoder isn't working right
//				val t = resource.url.getFile match {
//					case s: String if (s.toLowerCase.endsWith(".png")) => TextureUtil.loadPNG(resource.url)
//					case _ => TextureUtil(ImageIO.read(resource.url))
//				}
				val t = TextureUtil(ImageIO.read(resource.url))
				map += resource.url -> t
				t
			}
		}
	}
}