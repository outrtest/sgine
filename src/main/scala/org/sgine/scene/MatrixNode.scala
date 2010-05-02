package org.sgine.scene

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.math.mutable.Matrix4

import org.sgine.property.ImmutableProperty
import org.sgine.property.MutableProperty

import org.sgine.scene.event.SceneEvent
import org.sgine.scene.event.SceneEventType

import org.sgine.work.Updatable

trait MatrixNode extends Node with Updatable {
	val localMatrix = new ImmutableProperty[Matrix4](Matrix4().identity())
	val worldMatrix = new ImmutableProperty[Matrix4](Matrix4().identity())
	
	// Listen for parent change to invalidate matrix
	listeners += EventHandler(parentChanged, ProcessingMode.Blocking)
	
	private val revalidateMatrix = new MutableProperty[Boolean](true)
	
	def invalidateMatrix(evt: Event = null) = {
		revalidateMatrix := true
		
		initUpdatable()
	}
	
	localMatrix().changeDelegate = () => invalidateMatrix()
	
	abstract override def update(time: Double) = {
		super.update(time)
		
		if ((revalidateMatrix != null) && (revalidateMatrix())) {
			refreshWorldMatrix()
			
			revalidateMatrix := false
		}
	}
	
	protected def refreshWorldMatrix() = {
		// Update to parent world matrix
		getParentWorldMatrix(parent) match {
			case m: Matrix4 => MatrixNode.matrixStore.set(m)
			case _ => MatrixNode.matrixStore.identity()
		}
		
		// Update local matrix
		updateLocalMatrix()
		
		// Multiply against local matrix
		MatrixNode.matrixStore.mult(localMatrix())
		
		// Apply to world matrix
		worldMatrix().set(MatrixNode.matrixStore)
		
		// Invalidate children if NodeContainer
		invalidateChildren(this)
	}
	
	protected def updateLocalMatrix() = {
	}
	
	protected def getParentWorldMatrix(parent: Node): Matrix4 = {
		parent match {
			case null => null
			case mn: MatrixNode => mn.worldMatrix()
			case _ => getParentWorldMatrix(parent.parent)
		}
	}
	
	private def invalidateChildren(n: Node): Unit = {
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
	
	private def parentChanged(evt: SceneEvent) = {
		if (evt.eventType == SceneEventType.ParentChanged) {
			invalidateMatrix()
		}
	}
}

object MatrixNode {
	private[scene] val matrixStore = Matrix4()
}