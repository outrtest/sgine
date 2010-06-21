package org.sgine.bounding.event

import org.sgine.bounding.Bounding

import org.sgine.event.BasicEvent
import org.sgine.event.Listenable

class BoundingChangeEvent(listenable: Listenable, val bounding: Bounding) extends BasicEvent(listenable)