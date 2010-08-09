package org.sgine.scene.ext

import org.sgine.bounding.event.BoundingChangeEvent

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.MutableProperty
import org.sgine.property.event.PropertyChangeDelegate

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer
import org.sgine.scene.event.SceneEvent
import org.sgine.scene.event.SceneEventType

import org.sgine.work.Updatable

import simplex3d.math._
import simplex3d.math.doublem._

trait MatrixNode extends WorldMatrixNode with Updatable {
	val localMatrix = new AdvancedProperty[Mat3x4d](Mat3x4d.Identity)
	
	// Listen for parent change to invalidate matrix
	listeners += EventHandler(parentChanged, ProcessingMode.Blocking)
	listeners += EventHandler(boundingChanged, ProcessingMode.Blocking)
	
	private val revalidateMatrix = new MutableProperty[Boolean](true)
	
	private val updatingThread = new ThreadLocal[Boolean] {
		override def initialValue() = false
	}
	
	def invalidateMatrix(evt: Event = null) = {
		if (!updatingThread.get()) {
			revalidateMatrix := true
			
			initUpdatable()
		}
	}
	
//	localMatrix().changeDelegate = () => invalidateMatrix()
	localMatrix.listeners += EventHandler(PropertyChangeDelegate((m: Mat3x4d) => invalidateMatrix()), ProcessingMode.Blocking)
	
	abstract override def update(time: Double) = {
		super.update(time)
		
		if ((revalidateMatrix != null) && (revalidateMatrix())) {
			revalidateMatrix := false
			
			updatingThread.set(true)
			refreshWorldMatrix()
			updatingThread.set(false)
		}
	}
	
	protected def refreshWorldMatrix() = {
		// Update to parent world matrix
		var store = getParentWorldMatrix(parent) match {
			case m: Mat3x4d => m
			case _ => Mat3x4d.Identity
		}
		
		// Update local matrix
		updateLocalMatrix()
		
		// Multiply against local matrix
		store = localMatrix() concatenate store
//		MatrixNode.matrixStore.mult(localMatrix())
		
		// Apply to world matrix - causes WorldMatrixNode to invalidate children
		worldMatrix() := store
		worldMatrix.changedLocal()
	}
	
	protected def updateLocalMatrix() = {
	}
	
	protected def getParentWorldMatrix(parent: Node): Mat3x4d = {
		parent match {
			case null => null
			case mn: WorldMatrixNode => mn.worldMatrix()
			case _ => getParentWorldMatrix(parent.parent)
		}
	}
	
	private def parentChanged(evt: SceneEvent) = {
		if (evt.eventType == SceneEventType.ParentChanged) {
			invalidateMatrix()
		}
	}
	
	private def boundingChanged(evt: BoundingChangeEvent) = {
		invalidateMatrix()
	}
}