package org.sgine.scene.event

object SceneEventType extends Enumeration {
	type SceneEventType = Value
	
	val ChildAdded = Value
	val ChildRemoved = Value
	val ParentChanged = Value
}