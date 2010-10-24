package org.sgine.property

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.path.OPath

class PathPropertySpec extends FlatSpec with ShouldMatchers {
	"PathProperty" should "reference the dependent value" in {
		TestPathProperty.p1() should equal("One")
		TestPathProperty.p2() should equal("One")
		TestPathProperty.p3() should equal("One")
	}
	
	it should "modify properly when the dependent value changes" in {
		TestPathProperty.p1 := "Two"
		TestPathProperty.p1() should equal("Two")
		TestPathProperty.p2() should equal("Two")
		TestPathProperty.p3() should equal("Two")
	}
}

object TestPathProperty {
	val p1 = new AdvancedProperty[String]("One")
	val p2 = new PathProperty[String](OPath(this, "p1()"))
	val p3 = new PathProperty[String](OPath(this, "p2()"))
}