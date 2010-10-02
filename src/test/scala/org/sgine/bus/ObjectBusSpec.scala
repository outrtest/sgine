package org.sgine.bus

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ObjectBusSpec extends FlatSpec with ShouldMatchers {
	val bus = ObjectBus("testing")
	bus += ObjectNode[String](takeString, ObjectNode.HighestPriority)
	bus += ObjectNode[Test1](takeTest1, ObjectNode.HighestPriority)
	bus += ObjectNode[Test2](takeTest2, ObjectNode.NormalPriority)
	bus += ObjectNode[Test3](takeTest3, ObjectNode.LowPriority)
	
	var currentString = ""
	var test1 = 0
	var test2 = 0
	var test3 = 0
	
	"ObjectBus" should "process through successfully" in {
		currentString should equal("")
		bus process "Message 1"
		currentString should equal("Message 1")
	}
	
	it should "process Test1" in {
		test1 should equal(0)
		bus process new Test1
		test1 should equal(1)
	}
	
	it should "process Test2 as Test1" in {
		test1 should equal(1)
		test2 should equal(0)
		bus process new Test2
		test1 should equal(2)
		test2 should equal(1)
	}
	
	it should "not process Test3 because Test2 stopped it" in {
		test1 should equal(2)
		test2 should equal(1)
		test3 should equal(0)
		bus process new Test3
		test1 should equal(3)
		test2 should equal(2)
		test3 should equal(0)
	}
	
	def takeString(message: String) = {
		currentString = message
		Routing.Continue
	}
	
	def takeTest1(message: Test1) = {
		test1 += 1
		Routing.Continue
	}
	
	def takeTest2(message: Test2) = {
		test2 += 1
		Routing.Stop
	}
	
	def takeTest3(message: Test3) = {
		test3 += 1
		Routing.Continue
	}
	
	class Test1

	class Test2 extends Test1
	
	class Test3 extends Test2
}