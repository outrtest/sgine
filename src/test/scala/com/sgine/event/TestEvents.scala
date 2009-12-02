package com.sgine.event

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestEvents extends FlatSpec with ShouldMatchers {
	val listenable = new SimpleListenable()
	var handler:EventHandler = _
	
	"EventProcessor" should "be empty" in {
		listenable.listeners.size should equal (0)
	}
	
	it should "have one listener" in {
		handler = listenable.listeners += simpleListener _
		listenable.listeners.size should equal (1)
	}
	
	it should "allow changing of ProcessingMode" in {
		handler.processingMode = ProcessingMode.Blocking
	}
	
	"ProcessingMode" should "be different" in {
		ProcessingMode.Blocking should not equal (ProcessingMode.Asynchronous)
		ProcessingMode.Asynchronous should not equal (ProcessingMode.Normal)
		ProcessingMode.Normal should not equal (ProcessingMode.Blocking)
	}
	
	def simpleListener(evt: Event) = {
		println("simple listener!")
	}
}

class SimpleListenable extends Listenable {
	val parent = null
	val listeners = new EventProcessor(this)
}
