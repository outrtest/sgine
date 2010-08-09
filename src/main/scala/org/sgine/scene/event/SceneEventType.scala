package org.sgine.scene.event

import org.sgine.core.Enumerated
import org.sgine.core.Enumeration

sealed trait SceneEventType extends Enumeration

object SceneEventType extends Enumerated[SceneEventType] {
	
        case object ChildAdded extends SceneEventType
	case object ChildRemoved extends SceneEventType
	case object ParentChanged extends SceneEventType

        SceneEventType(ChildAdded, ChildRemoved, ParentChanged)
}