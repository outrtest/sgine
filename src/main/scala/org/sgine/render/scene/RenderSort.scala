package org.sgine.render.scene

import org.sgine.scene.ext.ColorNode
import org.sgine.scene.ext.MatrixNode
import org.sgine.scene.Node

object RenderSort extends Function2[Node, Node, Boolean] {
	def apply(n1: Node, n2: Node) = {
		if (n1.isInstanceOf[MatrixNode]) {
			if (n2.isInstanceOf[MatrixNode]) {
				val mn1 = n1.asInstanceOf[MatrixNode]
				val mn2 = n2.asInstanceOf[MatrixNode]
				
				mn1.worldMatrix().m23 < mn2.worldMatrix().m23
			} else {
				false
			}
		} else {
			true
		}
	}
}