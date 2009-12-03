package com.sgine.event

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import java.util.concurrent._

class TestEvents extends FlatSpec with ShouldMatchers {
	val listenable = new SimpleListenable()
	var handler1: EventHandler = _
	var handler2: EventHandler = _
	var handler3: EventHandler = _
	var listenerIncrement1: Int = 0
	var listenerIncrement2: Int = 0
	var listenerIncrement3: Int = 0
	var listenerIncrement4: Int = 0
	
	"EventProcessor" should "be empty" in {
		listenable.listeners.size should equal (0)
	}
	
	it should "have one listener" in {
		handler1 = listenable.listeners += simpleListener1 _
		handler1.filter = EventTypeFilter(classOf[SimpleEvent1])
		listenable.listeners.size should equal (1)
	}
	
	it should "have two listeners" in {
		handler2 = listenable.listeners += simpleListener2 _
		handler2.filter = EventTypeFilter(classOf[SimpleEvent2])
		listenable.listeners.size should equal (2)
	}
	
	it should "have three listeners" in {
		handler3 = listenable.listeners += simpleListener3 _
		handler3.filter = EventTypeFilter(classOf[SimpleEvent3])
		listenable.listeners.size should equal (3)
	}
	
	it should "allow changing of ProcessingMode" in {
		handler1.processingMode = ProcessingMode.Blocking
		handler3.processingMode = ProcessingMode.Asynchronous
	}
	
	it should "register the event occurrence in Blocking mode" in {
		listenerIncrement1 should equal (0)
		Event.enqueue(new SimpleEvent1(listenable))
		listenerIncrement1 should equal (1)
	}
	
	it should "register sub-classed events for filtered type" in {
		listenerIncrement1 should equal (1)
		Event.enqueue(new SimpleEvent4(listenable))
		listenerIncrement1 should equal (2)
	}
	
	it should "register the event occurrence in Normal mode" in {
		Event.enqueue(new SimpleEvent2(listenable))
		listenerIncrement2 should equal (0)
		Event.workManager.waitForIdle(5, TimeUnit.SECONDS) should equal (true)
		listenerIncrement2 should equal (1)
	}
	
	it should "register the event occurrence in Asynchronous mode" in {
		Event.enqueue(new SimpleEvent3(listenable))
		listenerIncrement3 should equal (0)
		Event.workManager.waitForIdle(5, TimeUnit.SECONDS) should equal (true)
		listenerIncrement3 should equal (1)
	}
	
	"EventListener" should "let through events for its generic type" in {
		val eh = listenable.listeners += exclusiveListener1 _
		eh.processingMode = ProcessingMode.Blocking
		listenable.listeners.size should equal (4)
		listenerIncrement4 should equal (0)
		Event.enqueue(new ExclusiveEvent1(listenable))
		listenerIncrement4 should equal (1)
		Event.enqueue(new SimpleEvent1(listenable))
		listenerIncrement4 should equal (1)
	}
	
	"ProcessingMode" should "be different" in {
		ProcessingMode.Blocking should not equal (ProcessingMode.Asynchronous)
		ProcessingMode.Asynchronous should not equal (ProcessingMode.Normal)
		ProcessingMode.Normal should not equal (ProcessingMode.Blocking)
	}
	
	def simpleListener1(evt: Event) = {
		println("listener1")
		listenerIncrement1 += 1
	}
	
	def simpleListener2(evt: Event) = {
		println("listener2")
		listenerIncrement2 += 1
	}
	
	def simpleListener3(evt: Event) = {
		println("listener3")
		listenerIncrement3 += 1
	}
	
	def exclusiveListener1(evt: ExclusiveEvent1) = {
		println("listener4")
		listenerIncrement4 += 1
	}
}

class SimpleListenable extends Listenable {
	val parent = null
	val listeners = new EventProcessor(this)
}

class SimpleEvent1(listenable: Listenable) extends Event(listenable)
class SimpleEvent2(listenable: Listenable) extends Event(listenable)
class SimpleEvent3(listenable: Listenable) extends Event(listenable)
class SimpleEvent4(listenable: Listenable) extends SimpleEvent1(listenable)
class ExclusiveEvent1(listenable: Listenable) extends Event(listenable)