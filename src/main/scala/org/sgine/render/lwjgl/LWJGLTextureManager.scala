package org.sgine.render.lwjgl

import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.ByteOrder

import org.sgine.util.GeneralReusableGraphic
import org.sgine.util.ReusableGraphic

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL14._

/**
 * TextureManager handles management of all textures. Creates Texture
 * objects.
 * 
 * @author Matt Hicks
 */
object LWJGLTextureManager {
	def apply(image: BufferedImage, x: Int, y: Int, width: Int, height: Int, mipmap: Boolean): LWJGLTexture = {
		if (isValidImage(image)) {
			if (image.getProperty("reusableGraphic") != "yes") {
				image.coerceData(true)
			}
			
			val textureFormat = GL_RGBA
			val imageFormat = GL_RGBA
			val imageType = GL_UNSIGNED_BYTE
			
			val data = new Array[Byte](width * 4)
			val raster = image.getRaster()
			val buffer = ByteBuffer.allocateDirect((width * height) * 4).order(ByteOrder.nativeOrder)
			for (i <- 0 until height) {
				raster.getDataElements(x, y + i, width, 1, data)
				buffer.put(data)
			}
			buffer.flip()
			
			new LWJGLTexture(new TextureUpdate(buffer, width, height, textureFormat, imageFormat, imageType, mipmap))
		} else {
			val rg = GeneralReusableGraphic
			val g = rg(image.getWidth, image.getHeight, -1)
			try {
				g.setComposite(AlphaComposite.Src)
				g.drawImage(image, 0, 0, image.getWidth, image.getHeight, null)
				g.dispose()
				
				apply(rg(), x, y, width, height, mipmap)
			} finally {
				rg.release()
			}
		}
	}
	
	def isValidImage(image:BufferedImage) = image.getColorModel() == ReusableGraphic.rgba;
}