package org.sgine.property

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.path._

class BindingSpec extends FlatSpec with ShouldMatchers {
	val p1 = new AdvancedProperty[String]("")
	val p2 = new AdvancedProperty[String]("Hello")
	val p3 = new AdvancedProperty[String]("World")
	val p4 = new AdvancedProperty[Int](1)
	
	var path: OPath[String] = _
	
	val t1 = new BindingTest1
	val t2 = new BindingTest2
	val t3 = new BindingTest3
	
	"Property Binding" should "assign value back when binding created" in {
		p1 bind p2
		p1() should equal("Hello")
	}
	
	it should "modify p1 without changing p2" in {
		p1 := "Test"
		p2() should equal("Hello")
		p1() should equal("Test")
	}
	
	it should "update p1 when changing p2" in {
		p2 := "Goodbye"
		p1() should equal("Goodbye")
		p2() should equal("Goodbye")
	}
	
	it should "synchronize all the way down when p2 is bound to p3" in {
		p2 bind p3
		p2() should equal("World")
		p1() should equal("World")
		p3() should equal("World")
	}
	
	it should "update p2 and p1 when p3 is modified" in {
		p3 := "Universe"
		p2() should equal("Universe")
		p1() should equal("Universe")
		p3() should equal("Universe")
	}
	
	it should "no longer update p1 when it is unbound from p2" in {
		p1 unbind p2
		p3 := "World"
		p2() should equal("World")
		p3() should equal("World")
		p1() should equal("Universe")
	}
	
	it should "no longer update p2 when it is unbound from p3" in {
		p2 unbind p3
		p3 := "Bar"
		p2() should equal("World")
		p3() should equal("Bar")
		p1() should equal("Universe")
	}
	
	"Path Binding" should "properly bind for a simple path" in {
		path = OPath[String](this, "p2")
		p1 bindPath path
		p1() should equal("World")
		p2() should equal("World")
		p3() should equal("Bar")
		p2 := "Goodbye"
		p1() should equal("Goodbye")
		p2() should equal("Goodbye")
		p3() should equal("Bar")
	}
	
	it should "properly unbind for a simple path" in {
		p1 unbindPath path
		p2 := "Hello"
		p1() should equal("Goodbye")
		p2() should equal("Hello")
		p3() should equal("Bar")
	}
	
	it should "auto-connect when a valid path arrives" in {
		path = OPath(t1, "p1().p2().p3")
		p1 bindPath path
		p1() should equal("Goodbye")
		t1.p1 := t2
		p1() should equal("Goodbye")
		t2.p2 := t3
		p1() should equal("")
		t3.p3 := "Hello World"
		p1() should equal("Hello World")
	}
	
	it should "auto-disconnect when the path is invalidated" in {
		t2.p2 := null
		p1() should equal("Hello World")
		t3.p3 := "Goodbye World"
		p1() should equal("Hello World")
	}
	
	it should "allow translation of values" in {
		p4 bind(p1, (s: String) => Some(s.length))
		p4() should equal(11)
		p1 := "Goodbye World"
		p4() should equal(13)
	}
	
	it should "disconnect binding properly" in {
		p4 unbind p1
		p1 := "Testing"
		p4() should equal(13)
	}
}

class BindingTest1 {
	val p1 = new AdvancedProperty[BindingTest2](null)
}

class BindingTest2 {
	val p2 = new AdvancedProperty[BindingTest3](null)
}

class BindingTest3 {
	val p3 = new AdvancedProperty[String]("")
}