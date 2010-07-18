package org.sgine.render

case class RenderSettings(alpha: Int = 0, depth: Int = 8, stencil: Int = 0, samples: Int = 0, bpp: Int = 0, auxBuffers: Int = 0, accumBPP: Int = 0, accumAlpha: Int = 0, stereo: Boolean = false, floatingPoint: Boolean = false)

object RenderSettings {
	val Default = new RenderSettings()
	val High = new RenderSettings(alpha = 4, depth = 8, stencil = 4, samples = 4)
}