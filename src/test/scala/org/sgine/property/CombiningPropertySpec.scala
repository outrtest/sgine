package org.sgine.property

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.path.OPath

class CombiningPropertySpec extends FlatSpec with ShouldMatchers {
	val sum = (i1: Int, i2: Int) => i1 + i2
	val mult = (i1: Int, i2: Int) => i1 * i2
	
	val p1 = new AdvancedProperty[Int](5)
	val p2 = new AdvancedProperty[Int](2, combine = p1, combineFunction = sum)
	val p3 = new AdvancedProperty[Int](8, combine = p2, combineFunction = sum)
	val p4 = new AdvancedProperty[Int](5, combine = p3, combineFunction = mult)
	
	val c1 = new CPT
	val c2 = new CPT
	c2.parent := c1
	val c3 = new CPT
	c3.parent := c2
	val c4 = new CPT
	c4.parent := c3
	
	"CombiningProperty" should "sum p1 and p2" in {
		p2() should equal(7)
	}
	
	it should "sum p3, p2, and p1" in {
		p3() should equal(15)
	}
	
	it should "multiply the sum of p3, p2, and p1 against p4" in {
		p4() should equal(75)
	}
	
	it should "determine visibility on CPT instances" in {
		c1.visible() should equal(true)
		c2.visible() should equal(true)
		c3.visible() should equal(true)
		c4.visible() should equal(true)
	}
	
	it should "show c3 and c4 invisible on CPT instances" in {
		c3.visible := false
		
		c1.visible() should equal(true)
		c2.visible() should equal(true)
		c3.visible() should equal(false)
		c4.visible() should equal(false)
	}
}

class CPT {
	val combiner = (b1: Boolean, b2: Boolean) => if ((b1) && (b2)) true else false
	val parent = new AdvancedProperty[CPT](null)
	val visible = new AdvancedProperty[Boolean](true, combine = new PathProperty[Boolean](OPath(this, "parent().visible()"), true), combineFunction = combiner)
}