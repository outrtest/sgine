package org.sgine.property.container

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.property._

class PropertyContainerSpec extends FlatSpec with ShouldMatchers {
	val container1 = PropertyContainer(null)
	val container2 = new PropertyContainer() {
		val p = new AdvancedProperty[Int](1)
	}
	val container3 = new MutablePropertyContainer() {
		val p1 = new AdvancedProperty[Int](1)
	}
	
	"PropertyContainer" should "be empty" in {
		container1.properties.size should equal (0)
	}
	
	it should "have one entry" in {
		container2.properties.size should equal (1)
	}
	
	it should "find 'p' by name" in {
		container2.property("p") should equal (Some(container2.p))
	}
	
	"MutablePropertyContainer" should "have one entry" in {
		container3.properties.size should equal (1)
	}
	
	it should "add a dynamic entry" in {
		container3.addProperty(new AdvancedProperty[Int](2, container3, "p2"))
	}
	
	it should "have two entries" in {
		container3.properties.size should equal (2)
	}
	
	it should "find 'p1' by name" in {
		container3.property("p1") should not equal (null)
	}
	
	it should "find 'p1' and it should equal container3.p1" in {
		container3.property("p1") should equal (Some(container3.p1))
	}
	
	it should "find 'p2' by name" in {
		container3.property("p2") should not equal (null)
	}
}
