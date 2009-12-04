package org.sgine.scene

import org.sgine.event._

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import java.util.concurrent._

class BasicSceneSpec extends FlatSpec with ShouldMatchers {
	var container:NodeContainer = _
	
	"GeneralNodeContainer" should "be empty" in {
		container = new GeneralNodeContainer()
		container.size should be (0)
	}
	
	it should "support adding listeners" in {
		container.listeners += listener1 _
		container.listeners.size should equal (1)
	}
	
	def listener1(evt: Event) = {
		println("listener1: " + evt)
	}
}