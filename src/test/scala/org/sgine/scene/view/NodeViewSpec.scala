package org.sgine.scene.view

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.core.ProcessingMode

import org.sgine.event._

import org.sgine.scene._
import org.sgine.scene.query._
import org.sgine.scene.view._
import org.sgine.scene.view.event._

class NodeViewSpec extends FlatSpec with ShouldMatchers {
	val container = new GeneralNodeContainer()
	val query = AllLeafQuery
	val view = NodeView(container, query, ProcessingMode.Blocking, null)
	val node = new TestNode()
	val subcontainer = new GeneralNodeContainer()
	
	var addedEvents = 0
	var removedEvents = 0
	
	"NodeView" should "be empty" in {
		view.size should equal (0)
	}
	
	it should "contain one listener added by the NodeView" in {
		container.listeners.size should equal (1)
	}
	
	it should "support adding listeners" in {
		view.listeners += EventHandler(addedToView, ProcessingMode.Blocking)
		view.listeners.size should equal (1)
		view.listeners += EventHandler(removedFromView, ProcessingMode.Blocking)
		view.listeners.size should equal (2)
	}
	
	it should "update when a node is added" in {
		container += node
		view.size should equal (1)
	}
	
	it should "invoke added event upon node add" in {
		addedEvents should equal (1)
	}
	
	it should "update when a node is removed" in {
		container -= node
		view.size should equal (0)
	}
	
	it should "invoke removed event upon node remove" in {
		removedEvents should equal (1)
	}
	
	it should "not receive add or remove events for an added container" in {
		container += subcontainer
		addedEvents should equal (1)
	}
	
	it should "update when a node is added to the sub-container" in {
		subcontainer += node
		view.size should equal (1)
	}
	
	it should "invoke added event upon node add to sub-container" in {
		addedEvents should equal (2)
	}
	
	it should "invoke removed event for node when sub-container is removed" in {
		container -= subcontainer
		removedEvents should equal (2)
	}
	
	it should "now be empty" in {
		view.size should equal (0)
	}
	
	def addedToView(evt: NodeAddedEvent) = {
		addedEvents += 1
	}
	
	def removedFromView(evt: NodeRemovedEvent) = {
		removedEvents += 1
	}
}

class TestNode extends Node