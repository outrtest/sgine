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

  def epsilonEqual(q1 : Quaternion, q2 : Quaternion) {
    val epsilon: Double = 0.00001
    q1.u should be (q2.u plusOrMinus epsilon)
    q1.x should be (q2.x plusOrMinus epsilon)
    q1.y should be (q2.y plusOrMinus epsilon)
    q1.z should be (q2.z plusOrMinus epsilon)
  }

  it should "convert to Euler angles and back" in {
    val q = Quaternion(11, -5, 3, 17).normalized
    epsilonEqual(q, (Quaternion.fromEulerAngles _) tupled q.toEulerAngles)
  }
  
  it should "convert to an angle and an axis, and back" in {
    val q = Quaternion(-3, 45, 30, -19).normalized
    epsilonEqual(q, (Quaternion.fromAngleAxis _) tupled q.toAngleAxis)
  }

  it should "convert to a rotation matrix and back" in {
    val q = Quaternion(16, 2, -4, 13).normalized
    epsilonEqual(q, Quaternion fromMatrix q.toMatrix)
  }

}
