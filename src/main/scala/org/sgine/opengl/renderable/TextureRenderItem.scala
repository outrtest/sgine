package org.sgine.opengl.renderable

import org.sgine.opengl.Texture
import org.sgine.math.Vector3
import org.lwjgl.opengl.GL11._;

class TextureRenderItem(val texture: Texture) extends RenderItem {
	def begin(renderable: Renderable, time: Double) = texture.draw()
	
	def vertex(renderable: Renderable, time: Double, index: Int) = {}
	
	def end(renderable: Renderable, time: Double) = {}
}