package org.sgine.math

import org.sgine.math
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * Tests Matrix4
 */
class Matrix4Spec extends FlatSpec with ShouldMatchers {

  "A new matrix" should "be the identity matrix" in {
    val m = new Matrix4()

    m.m00 should equal (1.0)
    m.m01 should equal (0.0)
    m.m02 should equal (0.0)
    m.m03 should equal (0.0)

    m.m10 should equal (0.0)
    m.m11 should equal (1.0)
    m.m12 should equal (0.0)
    m.m13 should equal (0.0)

    m.m20 should equal (0.0)
    m.m21 should equal (0.0)
    m.m22 should equal (1.0)
    m.m23 should equal (0.0)

    m.m30 should equal (0.0)
    m.m31 should equal (0.0)
    m.m32 should equal (0.0)
    m.m33 should equal (1.0)

  }

  it should "transform vectors correctly" in {

    val v = Vector3( 1, 2.5, -3 )

    val t = Matrix4.Identity.transform( v )
    
    t should equal (v)
  }

  // TODO: Test other transformations than identity


}
