package org.sgine.property

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class CombiningPropertySpec extends FlatSpec with ShouldMatchers {
	val sum = (i1: Int, i2: Int) => i1 + i2
	val mult = (i1: Int, i2: Int) => i1 * i2
	
	val p1 = new AdvancedProperty[Int](5)
	val p2 = new AdvancedProperty[Int](2, combine = p1, combineFunction = sum)
	val p3 = new AdvancedProperty[Int](8, combine = p2, combineFunction = sum)
	val p4 = new AdvancedProperty[Int](5, combine = p3, combineFunction = mult)
	
	"CombiningProperty" should "sum p1 and p2" in {
		p2() should equal(7)
	}
	
	it should "sum p3, p2, and p1" in {
		p3() should equal(15)
	}
	
	it should "multiply the sum of p3, p2, and p1 against p4" in {
		p4() should equal(75)
	}
}