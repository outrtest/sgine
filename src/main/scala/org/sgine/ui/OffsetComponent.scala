package org.sgine.ui

import org.sgine.core.ProcessingMode

import org.sgine.event.EventHandler
import org.sgine.event.Recursion

import org.sgine.ui.ext.Actual

trait OffsetComponent extends Component {
	val offset = new Actual(this)
	
	offset.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
}