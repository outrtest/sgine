package org.sgine.render

case class RenderList(list: List[() => Unit]) extends Function0[Unit] {
	def apply() = list.foreach(render)
	
	private val render = (r: () => Unit) => r()
}