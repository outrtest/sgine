package org.sgine.property

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class DependentPropertySpec extends FlatSpec with ShouldMatchers {
	val p1 = new AdvancedProperty[String]("One")
	val p2 = new AdvancedProperty[String]("Two", null, null, p1)
	
	"DependentProperty" should "reference the dependent value" in {
		p2() should equal (p1())
	}
	
	it should "reference the changed value upon change" in {
		p2 := "Testing"
		p2() should equal ("Testing")
	}
}