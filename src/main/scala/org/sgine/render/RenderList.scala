package org.sgine.render

case class RenderList(i: Iterable[() => Unit]) extends Renderable {
	def render() = i.foreach(renderItem)
	
	private val renderItem = (r: () => Unit) => r()
}