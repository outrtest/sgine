package org.sgine.path

import org.sgine.event.BasicEvent
import org.sgine.event.Listenable

class PathChangeEvent(val path: OPath) extends BasicEvent(path)