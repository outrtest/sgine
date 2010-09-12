package org.sgine.scene.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeDelegate

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer

import simplex3d.math._
import simplex3d.math.doublem._

trait WorldMatrixNode extends Node {
	val worldMatrix = new AdvancedProperty[Mat3x4d](Mat3x4d.Identity)
	
//	worldMatrix().changeDelegate = () => invalidateChildren(this)
	worldMatrix.listeners += EventHandler(PropertyChangeDelegate((m: Mat3x4d) => invalidateChildren(this)), ProcessingMode.Blocking)
	
	private def invalidateChildren(n: Node): Unit = {
		n match {
			case container: NodeContainer => {
				for (c <- container.children) c match {
					case mn: MatrixNode => mn.invalidateMatrix()
					case _ => invalidateChildren(c)
				}
			}
			case _ => // Not a container, so no children
		}
	}
}