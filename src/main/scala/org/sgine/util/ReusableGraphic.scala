package org.sgine.util

import java.awt._;
import java.awt.RenderingHints._
import java.awt.color._;
import java.awt.image._;
import java.util._;
import java.util.concurrent.locks._;

class ReusableGraphic {
	private var image:BufferedImage = _;
	private lazy val lock = new ReentrantLock();
	
	def apply(width:Int, height:Int):Graphics2D = {
		if (lock.tryLock()) {
			if ((image == null) || (image.getWidth < width) || (image.getHeight < height)) {
				var w:Int = width;
				var h:Int = height;
				if (image != null) {
					w = Math.max(image.getWidth, width);
					h = Math.max(image.getHeight, height);
					image.flush();
				}
				val raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, w, h, 4, null);
				val properties = new Hashtable[String, Object]()
				properties.put("reusableGraphic", "yes")
				image = new BufferedImage(ReusableGraphic.rgba, raster, false, properties);
			}
			val g = image.createGraphics();
			g.setBackground(ReusableGraphic.clear);
			g.clearRect(0, 0, image.getWidth, image.getHeight);
			g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY)
			g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
			g.setRenderingHint(KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_ON)
			g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC)
			g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY)
			g.setRenderingHint(KEY_STROKE_CONTROL, VALUE_STROKE_PURE)
			return g;
		}
		return null;
	}
	
	def apply(width:Int, height:Int, timeout:Long):Graphics2D = {
		var g:Graphics2D = null;
		val s = System.currentTimeMillis;
		while ((timeout == -1) || (s + timeout > System.currentTimeMillis)) {
			g = apply(width, height);
			if (g != null) return g;
			Thread.sleep(10);
		}
		return null;
	}
	
	def apply() = image;
	
	def release() = lock.unlock();
}

object ReusableGraphic {
	val clear = new Color(0.0f, 0.0f, 0.0f, 0.0f);
	val rgba = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			Array[Int](8, 8, 8, 8),
			true,
			false,
			Transparency.TRANSLUCENT,
			DataBuffer.TYPE_BYTE);
}