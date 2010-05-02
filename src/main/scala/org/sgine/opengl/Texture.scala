package org.sgine.opengl

import org.sgine.util._

import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.io._
import java.net._
import java.nio._

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL14._
import org.sgine.opengl.GLUtilities._

import scala.math._

class Texture {
	private lazy val id = generateId()
	@volatile private var update:TextureUpdate = _
	
	private var x:Int = _
	private var y:Int = _
	private var width:Int = _
	private var height:Int = _
	private var imageFormat:Int = _
	private var mipmap:Boolean = _
	
	private def generateId() = {
		val tmp = IntBuffer.allocate(1)
		glGenTextures(tmp)
		tmp.get(0)
	}
	
	def apply(image:BufferedImage, x:Int, y:Int, width:Int, height:Int, mipmap:Boolean): Unit = {
		if (isValidImage(image)) {
			if (image.getProperty("reusableGraphic") != "yes") {
				image.coerceData(true)
			}
			
			val textureFormat = GL_RGBA
			val imageFormat = GL_RGBA
			val imageType = GL_UNSIGNED_BYTE
			
			val data = new Array[Byte](width * 4)
			val raster = image.getRaster()
			val buffer = ByteBuffer.allocateDirect((width * height) * 4)
			buffer.order(ByteOrder.nativeOrder)
			for (i <- 0 until height) {
				raster.getDataElements(x, y + i, width, 1, data)
				buffer.put(data)
			}
			buffer.flip()
			
			update = TextureUpdate(buffer, width, height, textureFormat, imageFormat, imageType, mipmap)
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
	
	def isValidImage(image:BufferedImage) = image.getColorModel() == ReusableGraphic.rgba
	
	def draw() = {
		glBindTexture(GL_TEXTURE_2D, id)
		
		if (update != null) {
			updateTexture()
		}
		
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)
		
		var w:Float = width
		var h:Float = height
		if (!isNPOT) {
			w = nextPOT(width)
			h = nextPOT(height)
		}
		w = w / 2.0f
		h = h / 2.0f
	}
	
	private def updateTexture() = {
		val tu = update
		update = null
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if (tu.mipmap) GL_LINEAR_MIPMAP_LINEAR else GL_LINEAR)
		if (isVersion12) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
		}
		if ((tu.mipmap) && (isMipmap)) {
			glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_TRUE)
		}
		if ((tu.width != width) || (tu.height != height) || (tu.imageFormat != imageFormat) || (tu.mipmap != mipmap)) {
			var w = tu.width
			var h = tu.height
			if (isNPOT) {
				x = 0
				y = 0
			} else {
				w = nextPOT(w)
				h = nextPOT(h)
				x = round((w - tu.width) / 2.0f)
				y = round((h - tu.height) / 2.0f)
			}
			glTexImage2D(GL_TEXTURE_2D, 0, tu.imageFormat, w, h, 0, tu.textureFormat, tu.imageType, null.asInstanceOf[ByteBuffer])
			
			width = tu.width
			height = tu.height
			imageFormat = tu.imageFormat
			mipmap = tu.mipmap
		}
		glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, tu.width, tu.height, tu.textureFormat, tu.imageType, tu.buffer)
		if ((tu.mipmap) && (!isMipmap)) {
			println("**** TODO: implement glu mipmap generation ****")		// TODO: implement
		}
	}
	
	private def nextPOT(value:Int) = {
		var ret = 1
		while (ret < value) {
			ret <<= 1
		}
		ret
	}
}

case class TextureUpdate(buffer:ByteBuffer, width:Int, height:Int, textureFormat:Int, imageFormat:Int, imageType:Int, mipmap:Boolean)