package org.sgine.property

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.easing._

import org.sgine.property.animate._

import org.sgine.util.Time

class AnimatingPropertySpec extends FlatSpec with ShouldMatchers {
	val p1 = new NumericProperty(0.0)
	p1.animator = new LinearNumericAnimator(5.0)
	
	val p2 = new NumericProperty(0.0)
	p2.animator = new LinearNumericAnimator(5.0)
	
	val p3 = new NumericProperty(0.0)
	p3.animator = new EasingNumericAnimator(Linear.easeIn, 2.0, true)
	
	val p4 = new NumericProperty(0.0)
	p4.animator = new EasingNumericAnimator(Linear.easeIn, 2.0, true)
	
	"AnimatingProperty (Linear)" should "start at zero" in {
		p1() should equal(0.0)
	}
	
	it should "move less than 7.0 in one second" in {
		p1 := 10.0
		p1() should be < (10.0)
		Time.waitFor(1.0) {
			p1() > 7.0
		}
		p1() should be < 7.0
	}
	
	it should "move to 10.0 in two seconds" in {
		Time.waitFor(2.0) {
			p1() == 10.0
		}
		p1() should equal(10.0)
	}
	
	"AnimatingProperty (Linear - Numeric)" should "start at zero" in {
		p2() should equal(0.0)
	}
	
	it should "move less than 6.0 in one second" in {
		p2 := 10.0
		p2() should be < (10.0)
		Time.waitFor(1.0) {
			p2() > 6.0
		}
		p2() should be < 6.0
	}
	
	it should "move to 10.0 in two seconds" in {
		Time.waitFor(2.0) {
			p2() == 10.0
		}
		p2() should equal(10.0)
	}
	
	"AnimatingProperty (Easing)" should "start at zero" in {
		p3() should equal(0.0)
	}
	
	it should "move less than 6.0 in one second" in {
		p3 := 10.0
		p3() should be < (10.0)
		Time.waitFor(1.0) {
			p3() > 6.0
		}
		p3() should be < 6.0
	}
	
	it should "move to 10.0 in two seconds" in {
		Time.waitFor(2.0) {
			p3() == 10.0
		}
		p3() should equal(10.0)
	}
	
	"AnimatingProperty (Easing - Numeric)" should "start at zero" in {
		p4() should equal(0.0)
	}
	
	it should "move less than 6.0 in one second" in {
		p4 := 10.0
		p4() should be < (10.0)
		Time.waitFor(1.0) {
			p4() > 6.0
		}
		p4() should be < 6.0
	}
	
	it should "move to 10.0 in two seconds" in {
		Time.waitFor(2.0) {
			p4() == 10.0
		}
		p4() should equal(10.0)
	}
}