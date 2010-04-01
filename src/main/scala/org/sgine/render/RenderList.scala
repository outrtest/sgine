package org.sgine.render

case class RenderList(i: Iterable[() => Unit]) extends Function0[Unit] {
	def apply() = i.foreach(render)
	
	private val render = (r: () => Unit) => r()
}