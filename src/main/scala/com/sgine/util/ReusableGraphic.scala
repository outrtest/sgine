package com.sgine.util

import java.awt._;
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
				image = new BufferedImage(ReusableGraphic.rgba, raster, false, new Hashtable[String, Object]());
			}
			val g = image.createGraphics();
			g.setBackground(ReusableGraphic.clear);
			g.clearRect(0, 0, image.getWidth, image.getHeight);
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