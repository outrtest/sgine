package org.sgine.math

import org.sgine.math
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * Tests Vector3
 */
class Vector3Spec extends FlatSpec with ShouldMatchers {

  "A Vector3" should "default to all zero" in {
    val v = new Vector3()

    v.x should equal (0.0)
    v.y should equal (0.0)
    v.z should equal (0.0)
  }

  it should "have correct coordinate mapping" in {
    val v = new Vector3(1, 2, 3)

    v.x should equal (1)
    v.y should equal (2)
    v.z should equal (3)
  }

  it should "add correctly" in {
    Vector3(0,2,3) + (1,3,7) should equal (Vector3(1,5,10))
    Vector3(0,2,3) + Vector3(1,3,7) should equal (Vector3(1,5,10))
  }

  it should "subtract correctly" in {
    Vector3(0,2,3) - (1,3,7) should equal (Vector3(-1,-1,-4))
    Vector3(0,2,3) - Vector3(1,3,7) should equal (Vector3(-1,-1,-4))
  }

  it should "multiply with a scalar" in {
    Vector3(0,2,3) * 3 should equal (Vector3(0,6,9))
  }

  it should "divide with a scalar" in {
    Vector3(0,2,3) / 2 should equal (Vector3(0,1,1.5))
  }

  it should "support unary minus" in {
    -Vector3( 0, 0, 0) should equal (Vector3(0, 0, 0))
    -Vector3( -1, 2, 3) should equal (Vector3(1, -2, -3))
  }

  it should "calculate length correctly" in {
    new Vector3( -1, 0, 0 ).length should equal (1)
    new Vector3( 0, 0, 0 ).length should equal (0)
    new Vector3( 0, 1, 0 ).length should equal (1)
    new Vector3( 0, 3, 4 ).length should equal ( 5 )
    new Vector3( 1, 2, 3 ).length should equal ( Math.sqrt(14) )
  }

  it should "calculate squared length correctly" in {
    new Vector3( -1, -2, 0 ).lengthSquared should equal (5)
    new Vector3( 0, 0, 0 ).lengthSquared should equal (0)
    new Vector3( 0, 1, 0 ).lengthSquared should equal (1)
    new Vector3( 0, 3, 4 ).lengthSquared should equal ( 25 )
    new Vector3( 1, 2, 3 ).lengthSquared should equal ( 14 )
  }

  it should "convert to string correctly" in {
    new Vector3(1.1, 2, -3).toString should equal ( "Vector3(1.1, 2.0, -3.0)" )
  }

  it should "convert to a tuple" in {
    new Vector3(1.1, 2, -3).toTuple should equal ( (1.1, 2.0, -3.0) )
  }

  it should "convert to a list" in {
    new Vector3(1.1, 2, -3).toList should equal ( 1.1 :: 2.0 :: -3.0 :: Nil )
  }

  it should "have default objects for common vectors" in {
    Vector3.Origo should equal ( Vector3(0,0,0) )
    Vector3.Zero should equal ( Vector3(0,0,0) )
    Vector3.UnitX should equal ( Vector3(1,0,0) )
    Vector3.UnitY should equal ( Vector3(0,1,0) )
    Vector3.UnitZ should equal ( Vector3(0,0,1) )
    Vector3.Ones  should equal ( Vector3(1,1,1) )
  }

  it should "calculate dot product properly" in {

    Vector3( 0, 0, 0 ) * Vector3( 0, 0, 0 ) should equal( 0 )
    Vector3( 1, 2, 3 ) * Vector3( 2, 3, 4 ) should equal( 2 + 6 + 12 )
    Vector3( -1, 0, -1 ) * Vector3( 0, 1, 1 ) should equal( -1 )

  }

  it should "calculate cross product properly" in {

    Vector3( 0, 0, 0 ) cross Vector3( 0, 0, 0 ) should equal( Vector3( 0, 0, 0 ) )
    Vector3( 1, 0, 0 ) cross Vector3( 0, 1, 0 ) should equal( Vector3( 0, 0, 1 ) )
    Vector3( 1, 0, 0 ) cross Vector3( 0, -1, 0 ) should equal( Vector3( 0, 0, -1 ) )
    Vector3( 2, 0, 0 ) cross Vector3( 0, 0, -3 ) should equal( Vector3( 0, 6, 0 ) )
    Vector3( 1, 2, 3 ) cross Vector3( 2, 3, 4 ) should equal( Vector3( 2*4 - 3*3, 3*2 - 1*4, 1*3 - 2*2 ) )

  }

  it should "calculate distance correctly" in {
    Vector3( 1, 3, 4 ) distance Vector3( 1, 0, 0 ) should equal (5)
    Vector3( 0, 2, 0 ) distance Vector3( 0, -1, 0 ) should equal (3)
  }

  it should "calculate squared distance correctly" in {
    Vector3( 1, 3, 4 ) distanceSquared Vector3( 1, 0, 0 ) should equal (25)
    Vector3( 0, 2, 0 ) distanceSquared Vector3( 0, -1, 0 ) should equal (9)
  }

  it should "normalize correctly" in {
    val d2 = 1.0 / Math.sqrt( 2 )
    val d3 = 1.0 / Math.sqrt( 3 )
    val epsilon: Double = 0.00001

    Vector3(0,  0,  0).normalized should equal( Vector3.Zero )
    Vector3(1, -1,  0).normalized should equal( Vector3( d2, -d2, 0 ) )
    Vector3(2,  0,  0).normalized should equal( Vector3( 1, 0, 0 ) )
    Vector3(1,  1,  1).normalized should equal( Vector3( d3, d3, d3 ) )
    Vector3(1,  2, -2).normalized should equal( Vector3( 1.0/3, 2.0/3, -2.0/3 ) )

    Vector3(0,  0,  0).normalized.length should be (0.0 plusOrMinus epsilon)
    Vector3(1, -1,  0).normalized.length should be (1.0 plusOrMinus epsilon)
    Vector3(2,  0,  0).normalized.length should be (1.0 plusOrMinus epsilon)
    Vector3(1,  1,  1).normalized.length should be (1.0 plusOrMinus epsilon)
    Vector3(1,  2, -1).normalized.length should be (1.0 plusOrMinus epsilon)
  }

/* Didn't get this conversation to work yet

  it should "be created implicitly from a tuple" in {
    val v : Vector3 = (1, 2, 3)
    v should equal (Vector3(1, 2, 3))
  }
*/

}
