package org.sgine.path

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.event._

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
	
	it should "resolve method1 via ReflectionResolver" in {
		TestStructure.resolve("container1.method1") should equal(Some(TestStructure))
	}
	
	it should "resolve complex path" in {
		TestStructure.resolve("container1.method1.prop3.value.prop2.value") should equal(Some("World"))
	}
	
	it should "resolve structure correctly for complex path" in {
		val structure = TestStructure.resolveStructure("container1.method1.prop3.value.prop2.value")
		structure.length should equal (6)
		structure(5) should equal(Some(TestStructure.container1))
		structure(4) should equal(Some(TestStructure))
		structure(3) should equal(Some(TestStructure.prop3))
		structure(2) should equal(Some(TestStructure.container1))
		structure(1) should equal(Some(TestStructure.container1.prop2))
		structure(0) should equal(Some("World"))
	}
	
	it should "receive PathChangeEvent properly" in {
		val path = OPath(TestStructure, "prop4.value.prop2.value")
		var pathChangeCount = 0
		def pathChanged(evt: PathChangeEvent) = {
			pathChangeCount += 1
		}
		path.listeners += EventHandler(pathChanged _, ProcessingMode.Blocking)
		pathChangeCount should equal(0)
		TestStructure.prop4 := TestStructure.container1
		pathChangeCount should equal(1)
	}
}

object TestStructure extends PropertyContainer {
	val prop1 = new AdvancedProperty[String]("Hello", this)
	val container1 = new PropertyContainer() {
		override val parent = TestStructure
		
		val prop2 = new AdvancedProperty[String]("World", this)
		
		def method1 = TestStructure
	}
	
	val field1 = "Reflective"
	
	val prop3 = new AdvancedProperty[PropertyContainer](container1, this)
	
	val prop4 = new AdvancedProperty[PropertyContainer](null, this)
}