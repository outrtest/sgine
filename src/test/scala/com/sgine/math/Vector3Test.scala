package com.sgine.math

import com.sgine.math
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * Tests Vector3
 */
class Vector3Test extends FlatSpec with ShouldMatchers {

  "A vector" should "default to all zero" in {
    val v = new Vector3()

    v.x should equal (0.0)
    v.y should equal (0.0)
    v.z should equal (0.0)
  }

  it should "add correctly" in {
    Vector3(0,2,3) + (1,3,7) should equal (Vector3(1,5,10))
  }

  it should "subtract correctly" in {
    Vector3(0,2,3) - Vector3(1,3,7) should equal (Vector3(-1,-1,-4))
  }

  it should "multiply with a scalar" in {
    Vector3(0,2,3) * 3 should equal (Vector3(0,6,9))
  }

  it should "divide with a scalar" in {
    Vector3(0,2,3) / 2 should equal (Vector3(0,1,1.5))
  }


/* Didn't get this conversation to work yet

  it should "be created implicitly from a tuple" in {
    val v : Vector3 = (1, 2, 3)
    v should equal (Vector3(1, 2, 3))
  }
*/

}
