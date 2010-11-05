package org.sgine.ui

import org.sgine.scene.Node

class Container extends AbstractContainer {
	val layout = _layout
	
	override def +=(node: Node) = super.+=(node)
	
	override def -=(node: Node) = super.-=(node)
	
	def drawComponent() = {
	}
}