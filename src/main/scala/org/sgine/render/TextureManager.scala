package org.sgine.render

import java.net.URL

import javax.imageio.ImageIO

import org.sgine.core.Resource

object TextureManager {
	private val mapLocal = new ThreadLocal[Map[URL, Texture]]
	
	def apply(resource: Resource) = {
		synchronized {
			val map = mapLocal.get match {
				case null => Map.empty[URL, Texture]
				case m => m
			}
			
			if (map.contains(resource.url)) {
				map(resource.url)
			} else {
				val t = resource.url.getFile match {
					case s: String if (s.toLowerCase.endsWith(".png")) => TextureUtil.loadPNG(resource.url)
					case _ => TextureUtil(ImageIO.read(resource.url))
				}
				mapLocal.set(map + (resource.url -> t))
				t
			}
		}
	}
}