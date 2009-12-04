package org.sgine.event

import org.sgine.work._
import org.sgine.work.unit._

class Event (val listenable: Listenable)

object Event {
	var workManager = DefaultWorkManager
	
	def enqueue(evt: Event) = {
		// Pre-Process Event on Listenable
		evt.listenable.preProcessEvent(evt)
		
		// Process Event on blocking handlers
		evt.listenable.listeners.filter(_.processingMode == ProcessingMode.Blocking).foreach(_.process(evt))
		
		// Walk up the hierarchy
		processRecursive(evt.listenable.parent, evt)
		
		// Enqueue Normal-blocking event
		workManager += NormalEventWorkUnit(evt)
		
		// Iterate over and enqueue asynchronous entries
		evt.listenable.listeners.filter(_.processingMode == ProcessingMode.Asynchronous).foreach(workManager += AsynchronousWorkUnit(_, evt))
	}
	
	private def processRecursive(l: Listenable, evt: Event):Unit = {
		if (l != null) {
			l.listeners.filter(_.recursive).filter(_.processingMode == ProcessingMode.Blocking).foreach(_.process(evt))
			
			processRecursive(l.parent, evt)
		}
	}
}

case class AsynchronousWorkUnit(h: EventHandler, evt: Event) extends Function0[Unit] {
	def apply() = {
		h.process(evt)
	}
}

case class NormalEventWorkUnit(evt: Event) extends BlockingWorkUnit(evt.listenable) {
	def apply() = {
		evt.listenable.listeners.filter(_.processingMode == ProcessingMode.Normal).foreach(_.process(evt));
	}
}