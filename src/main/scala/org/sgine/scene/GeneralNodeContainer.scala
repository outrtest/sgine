package org.sgine.scene

class GeneralNodeContainer extends AbstractNodeContainer with MutableNodeContainer {
	override def +=(node: Node) = super.+=(node)
	
	override def -=(node: Node) = super.-=(node)
}