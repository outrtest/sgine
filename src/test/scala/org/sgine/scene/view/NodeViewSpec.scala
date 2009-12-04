package org.sgine.scene.view

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.event._

import org.sgine.scene._
import org.sgine.scene.query._
import org.sgine.scene.view._
import org.sgine.scene.view.event._

class NodeViewSpec extends FlatSpec with ShouldMatchers {
	val container = new GeneralNodeContainer()
	val query = AllQuery
	val view = NodeView(container, query, false)
	val node = new TestNode()
	
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
	
	def addedToView(evt: NodeAddedEvent) = {
		addedEvents += 1
	}
	
	def removedFromView(evt: NodeRemovedEvent) = {
		removedEvents += 1
	}
}

class TestNode extends Node