package org.sgine.scene

import org.sgine.event._

import org.sgine.scene.event._

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import java.util.concurrent._

class BasicSceneSpec extends FlatSpec with ShouldMatchers {
	var container: MutableNodeContainer = _
	var listener1Handler: EventHandler = _
	var childNode: TestNode = new TestNode()
	var addedCount: Int = 0
	var removedCount: Int = 0
	
	"GeneralNodeContainer" should "be empty" in {
		container = new GeneralNodeContainer()
		container.size should be (0)
	}
	
	it should "support adding listeners" in {
		listener1Handler = container.listeners += EventHandler(listener1, ProcessingMode.Blocking, Recursion.Parents)
		container.listeners.size should equal (1)
	}
	
	it should "support removing listeners" in {
		container.listeners -= listener1Handler
		container.listeners.size should equal (0)
	}
	
	it should "throw an event when a child is added" in {
		container.listeners += EventHandler(containerEvent, ProcessingMode.Blocking, Recursion.Children)
		addedCount should equal (0)
		removedCount should equal (0)
		container += childNode
		addedCount should equal (1)
		removedCount should equal (0)
	}
	
	it should "throw an event when a child is removed" in {
		addedCount should equal (1)
		removedCount should equal (0)
		container -= childNode
		addedCount should equal (1)
		removedCount should equal (1)
	}
	
	def listener1(evt: Event) = {
		println("listener1: " + evt)
	}
	
	def containerEvent(evt: NodeContainerEvent) = {
		if (evt.eventType == SceneEventType.ChildAdded) {
			addedCount += 1
		} else if (evt.eventType == SceneEventType.ChildRemoved) {
			removedCount += 1
		}
	}
}

class TestNode extends Node