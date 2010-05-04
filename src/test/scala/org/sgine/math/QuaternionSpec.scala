/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgine.math

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.tools.Runner

class QuaternionSpec extends FlatSpec with ShouldMatchers {

  "A Quaterion" should "default to one" in {
    val q = new Quaternion()

    q.u should equal (1.0)
    q.x should equal (0.0)
    q.y should equal (0.0)
    q.z should equal (0.0)
  }

  it should "have correct coordinate mapping" in {
    val q = Quaternion(1, 2, 3, 4)

    q.u should equal (1)
    q.x should equal (2)
    q.y should equal (3)
    q.z should equal (4)
  }

  it should "allow different ways of construction" in {
    val q0 = Quaternion(1, 2, 3, 4)
    val q1 = Quaternion((1, 2, 3, 4))
    val q2 = Quaternion(1, Vector3(2,3,4))
    q0 should equal (q1)
    q1 should equal (q2)
    q2 should equal (q0)
  }

  it should "multiply correctly with a scalar" in {
    Quaternion(1,2,3,5) * 11 should equal (Quaternion(11,22,33,55))
  }

  it should "be divided correctly by a scalar" in {
    Quaternion(11,22,33,55) / 11 should equal (Quaternion(1,2,3,5))
  }

  it should "be negated correctly" in {
    - Quaternion(1,2,-3,-5) should equal (Quaternion(-1,-2,3,5))
  }

  it should "be conjugated correctly" in {
    Quaternion(1,2,-3,-5).conjugated should equal (Quaternion(1,-2,3,5))
  }

  it should "be normalized correctly" in {
    Quaternion(1,2,3,4).normalized should equal (Quaternion.normalized(1,2,3,4))
    Quaternion(1,3,5,1).normalized should equal (Quaternion(1.0/6, 3.0/6, 5.0/6, 1.0/6))
  }

  it should "add correctly" in {
    Quaternion(0,2,3,5) + Quaternion(1,3,7,2) should equal (Quaternion(1,5,10,7))
  }

  it should "subtract correctly" in {
    Quaternion(0,2,3,5) - Quaternion(1,3,7,2) should equal (Quaternion(-1,-1,-4,3))
  }

  it should "convert from Euler angles and back" in {
    val epsilon: Double = 0.00001
    val (a,b,c) = Quaternion.fromEulerAngles(0.2, 0.3, 0.4).toEulerAngles
    a should equal (0.2 plusOrMinus epsilon)
    b should equal (0.3 plusOrMinus epsilon)
    c should equal (0.4 plusOrMinus epsilon)
  }
}
