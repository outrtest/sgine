package org.sgine.render

case class RenderList(i: () => Unit*) extends Renderable {
	def render(renderer: Renderer) = i.foreach(renderItem)
	
	private val renderItem = (r: () => Unit) => r()
}