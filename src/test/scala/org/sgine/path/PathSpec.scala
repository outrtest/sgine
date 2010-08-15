package org.sgine.path

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.log._

import org.sgine.property._
import org.sgine.property.container._

class PathSpec extends FlatSpec with ShouldMatchers {
	"PathSpec" should "resolve value of prop1" in {
		TestStructure.resolve("prop1.value") should equal(Some("Hello"))
	}
	
	it should "resolve prop1" in {
		TestStructure.resolve("prop1") should equal(Some(TestStructure.prop1))
	}
	
	it should "resolve name of prop1" in {
		TestStructure.resolve("prop1.name") should equal(Some("prop1"))
	}
	
	it should "resolve value of prop2" in {
		TestStructure.resolve("container1.prop2.value") should equal(Some("World"))
	}
	
	it should "not resolve prop3" in {
		TestStructure.resolve("container1.prop3") should equal(None)
	}
	
	it should "resolve field1 via ReflectionResolver" in {
		TestStructure.resolve("field1") should equal(Some("Reflective"))
	}
}

object TestStructure extends PropertyContainer {
	val prop1 = new AdvancedProperty[String]("Hello", this)
	val container1 = new PropertyContainer() {
		override val parent = TestStructure
		
		val prop2 = new AdvancedProperty[String]("World", this)
	}
	
	val field1 = "Reflective"
}