package org.sgine.scene.ext

import org.sgine.math.mutable.Matrix4

import org.sgine.property.ImmutableProperty

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer

trait WorldMatrixNode extends Node {
	val worldMatrix = new ImmutableProperty[Matrix4](Matrix4().identity())
	
	worldMatrix().changeDelegate = () => invalidateChildren(this)
	
	def invalidateChildren(n: Node): Unit = {
		n match {
			case container: NodeContainer => {
				for (c <- container) c match {
					case mn: MatrixNode => mn.invalidateMatrix()
					case _ => invalidateChildren(c)
				}
			}
			case _ => // Not a container, so no children
		}
	}
}