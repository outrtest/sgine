package com.sgine.event

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestEvents extends FlatSpec with ShouldMatchers {
	val ep = new EventProcessor();
	
	"EventProcessor" should "be empty" in {
		ep.length should equal (0)
	}
}
