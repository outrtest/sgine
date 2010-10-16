package org.sgine.ui

import org.sgine.core.ProcessingMode

import org.sgine.event.EventHandler

import org.sgine.property.AdvancedProperty
import org.sgine.property.Property
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.ui.skin.Skin

trait SkinnedComponent extends Component {
	protected def themeSkin: Property[Skin] = null
	
	val skin = new AdvancedProperty[Skin](null, this, dependency = themeSkin)
	skin.listeners += EventHandler(skinChanged, ProcessingMode.Blocking)
	
	abstract override protected def predrawables = drawSkin _ :: super.predrawables
	
	def drawSkin() = {
		skin() match {
			case null =>
			case s => s.draw()
		}
	}
	
	private def skinChanged(evt: PropertyChangeEvent[Skin]) = {
		if (evt.oldValue != null) {
			evt.oldValue.disconnect(this)
		}
		if (evt.newValue != null) {
			evt.newValue.connect(this)
		}
	}
}

object SkinnedComponent {
	def apply() = new SkinnedComponent() {
		def drawComponent() = {
		}
	}
}