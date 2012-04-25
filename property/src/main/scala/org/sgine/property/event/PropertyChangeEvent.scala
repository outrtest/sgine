package org.sgine.property.event

import org.sgine.event.ChangeEvent
import org.sgine.property.Property

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class PropertyChangeEvent(property: Property[_], oldValue: Any, newValue: Any) extends ChangeEvent