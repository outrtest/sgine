package org.sgine.render.scene

import org.sgine.scene.ext.ColorNode
import org.sgine.scene.ext.MatrixNode
import org.sgine.scene.Node

object RenderSort extends Function2[Node, Node, Int] {
	def apply(n1: Node, n2: Node) = {
		if (n1.isInstanceOf[MatrixNode]) {
			if (n2.isInstanceOf[MatrixNode]) {
				val mn1 = n1.asInstanceOf[MatrixNode]
				val mn2 = n2.asInstanceOf[MatrixNode]
				
				// TODO: implement better sorting:
				/*
				 * val dir = entityWorldMatrix(3) - camWorldMatrix(3)
				 * val distSquare = dot(dir, dir);
				 */
				
				if (mn1.worldMatrix().m23 < mn2.worldMatrix().m23) {
					-1
				} else if (mn1.worldMatrix().m23 > mn2.worldMatrix().m23) {
					1
				} else {
					0
				}
			} else {
				-1
			}
		} else {
			1
		}
	}
}