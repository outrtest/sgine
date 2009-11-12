package com.sgine.math

import com.sgine.math
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * Tests Vec
 */
class VecTest extends FlatSpec with ShouldMatchers {

  "A vector" should "default to all zero" in {
    val v = new Vec()

    v.x should equal (0.0)
    v.y should equal (0.0)
    v.z should equal (0.0)
  }

  it should "add correctly" in {
    Vec(0,2,3) + (1,3,7) should equal (Vec(1,5,10))
  }

  it should "subtract correctly" in {
    Vec(0,2,3) - Vec(1,3,7) should equal (Vec(-1,-1,-4))
  }

  it should "multiply with a scalar" in {
    Vec(0,2,3) * 3 should equal (Vec(0,6,9))
  }

  it should "divide with a scalar" in {
    Vec(0,2,3) / 2 should equal (Vec(0,1,1.5))
  }


/* Didn't get this conversation to work yet

  it should "be created implicitly from a tuple" in {
    val v : Vec = (1, 2, 3)
    v should equal (Vec(1, 2, 3))
  }
*/

}
