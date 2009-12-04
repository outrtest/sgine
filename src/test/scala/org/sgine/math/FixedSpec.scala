package org.sgine.math

import org.sgine.math
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * Tests Fixed point math
 */
@Deprecated
class FixedSpec  extends FlatSpec with ShouldMatchers {

  "A Fixed point value" should "have the correct division factor" in {
    Fixed.ShiftAmount should equal (16)
    Fixed.decimalDivisor should equal (65536)  
  }

  it should "have common constants" in {
    Fixed.One.toDouble should equal (1.0)
    Fixed.Zero.toDouble should equal (0.0)
  }

  it should "have working math operations" in {
    Fixed.make( 2 ) + Fixed.make( 3 ) should equal( Fixed.make( 5 ) )
    (Fixed.make( 2 ) + Fixed.make( 3 )).toDouble should equal( 5.0 )
    (Fixed.make( -1 ) + Fixed.make( 2 )).toDouble should equal( 1.0 )
    (Fixed.make( -1.0 ) + Fixed.make( 2.0 )).toDouble should equal( 1.0 )
    (Fixed.make( -0.5 ) * Fixed.make( 2.0 )).toDouble should equal( -1.0 )
    ((Fixed.make( -2.4 ) - Fixed.make( 0.6 )) / Fixed.make( 3f )).toDouble should be( -1.0 plusOrMinus 0.0001 )
    (Fixed.make( 3.0 ) * Fixed.make(5)).toDouble should equal( 15.0 ) 
  }

}
