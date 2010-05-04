package org.sgine.math

import scala.math._

/*
 * Partially based on code from jMonkeyEngine's Quaternion class
 * (Mark Powell, Joshua Slack)
 */

object Quaternion {

  type D = Double

  def apply(u : D, x : D, y : D, z : D) = {
    val q = new Quaternion()
    q.u = u
    q.x = x
    q.y = y
    q.z = z
    q
  }

  def apply(tuple4 : (D, D, D, D)) : Quaternion =
    Quaternion(tuple4._1, tuple4._2, tuple4._3, tuple4._4)

  def apply(real : D, imag : Vector3) : Quaternion =
    Quaternion(real, imag.x, imag.y, imag.z)

  def normalized(u : D, x : D, y : D, z : D) = {
     val abs = sqrt(u * u + x * x + y * y + z * z)
     Quaternion(u / abs, x / abs, y / abs, z / abs)
  }

  val Zero = Quaternion(0, 0, 0, 0)

  val One = Quaternion(1, 0, 0, 0)

  def fromEulerAngles(yaw : D, roll : D,  pitch :D) : Quaternion = {
    val halfPitch = pitch / 2
    val sinPitch = sin(halfPitch)
    val cosPitch = cos(halfPitch)
    val halfRoll = roll / 2
    val sinRoll = sin(halfRoll)
    val cosRoll = cos(halfRoll)
    val halfYaw = yaw / 2
    val sinYaw = sin(halfYaw)
    val cosYaw = cos(halfYaw)

    val cosRollXcosPitch = cosRoll * cosPitch
    val sinRollXsinPitch = sinRoll * sinPitch
    val cosRollXsinPitch = cosRoll * sinPitch
    val sinRollXcosPitch = sinRoll * cosPitch

    val u = (cosRollXcosPitch * cosYaw - sinRollXsinPitch * sinYaw)
    val x = (cosRollXcosPitch * sinYaw + sinRollXsinPitch * cosYaw)
    val y = (sinRollXcosPitch * cosYaw + cosRollXsinPitch * sinYaw)
    val z = (cosRollXsinPitch * cosYaw - sinRollXcosPitch * sinYaw)
   
    normalized(u, x, y, z)
  }

  def fromAngleAxis(angle : D, axis : Vector3) : Quaternion = {
    val a = axis.normalized
    if (a.x == 0 && a.y == 0 && a.z == 0) One
    else {
      val halfAngle = angle / 2
      val sinus = sin(halfAngle)
      Quaternion(cos(halfAngle), sinus * a.x, sinus * a.y, sinus * a.z)
    }
  }

  // Use the Graphics Gems code, from
  // ftp://ftp.cis.upenn.edu/pub/graphics/shoemake/quatut.ps.Z
  def fromMatrix(m : Matrix4) : Quaternion = {
        // the trace is the sum of the diagonal elements; see
        // http://mathworld.wolfram.com/MatrixTrace.html
        val t = m.m00 + m.m11 + m.m22

        // we protect the division by s by ensuring that s>=1
        if (t >= 0) { // |w| >= .5
            val s = sqrt(t+1) // |s|>=1 ...
	    val q = 0.5 / s
	    Quaternion(s / 2, (m.m21 - m.m12) * q, (m.m02 - m.m20) * q, (m.m10 - m.m01) * q)
        } else if ((m.m00 > m.m11) && (m.m00 > m.m22)) {
            val s = sqrt(1.0 + m.m00 - m.m11 - m.m22) // |s|>=1
	    val q = 0.5 / s
            Quaternion((m.m21 - m.m12) * q, s / 2, (m.m10 + m.m01) * q, (m.m02 + m.m20) * q)
        } else if (m.m11 > m.m22) {
            val s = sqrt(1.0 + m.m11 - m.m00 - m.m22) // |s|>=1
            val q = 0.5 / s
	    Quaternion((m.m02 - m.m20) * q, (m.m10 + m.m01) * q, s / 2, (m.m21 + m.m12) * q)
        } else {
            val s = sqrt(1.0 + m.m22 - m.m00 - m.m11) // |s|>=1
            val q = 0.5f / s;
	    Quaternion((m.m10 - m.m01) * q, (m.m02 + m.m20) * q, (m.m21 + m.m12) * q, s / 2)
        }
  }


  //SLERP
  def interpolate(q1 : Quaternion, q2 : Quaternion, t : D) : Quaternion =
    if (q1 == q2) q1 else {
      val r = q1 dot q2
      val (result, q_2) = if (r < 0.0) (-r, -q2) else (r, q2)

      val (scale0, scale1) = if (0.95 > result) {
	val theta = acos(result)
	val invSinTheta = 1/sin(theta)
	(sin((1 - t) * theta) * invSinTheta, sin((t * theta)) * invSinTheta)
      } else (1 - t, t)

      Quaternion(scale0 * q1.u + scale1 * q_2.u,
		 scale0 * q1.x + scale1 * q_2.x,
		 scale0 * q1.y + scale1 * q_2.y,
		 scale0 * q1.z + scale1 * q_2.z)

    }

}

class Quaternion {
  import Quaternion.D

  var _u: D = 1.0
  var _x: D = 0.0
  var _y: D = 0.0
  var _z: D = 0.0

  def u = _u
  protected def u_=(_u : D) = this._u = _u
  def x = _x
  protected def x_=(_x : D) = this._x = _x
  def y = _y
  protected def y_=(_y : D) = this._y = _y
  def z = _z
  protected def z_=(_z : D) = this._z = _z

  def *(d : D) : Quaternion = Quaternion(u * d, x * d, y * d, z * d)
  def /(d : D) : Quaternion = Quaternion(u / d, x / d, y / d, z / d)

  def unary_- : Quaternion = Quaternion(-u, -x, -y, -z)
  def +(that : Quaternion) : Quaternion =
    Quaternion(this.u + that.u, this.x + that.x, this.y + that.y, this.z + that.z)
  def -(that : Quaternion) : Quaternion =
    Quaternion(this.u - that.u, this.x - that.x, this.y - that.y, this.z - that.z)
  def *(that : Quaternion) : Quaternion =
    Quaternion(this.u * that.u - this.x * that.x - this.y * that.y - this.z * that.z,
               this.u * that.x + this.x * that.u + this.y * that.z - this.z * that.y,
               this.u * that.y - this.x * that.z + this.y * that.u + this.z * that.x,
               this.u * that.z + this.x * that.y - this.y * that.x + this.z * that.u)
  def dot(that : Quaternion) : D =
      this.u * that.u + this.x * that.x + this.y * that.y + this.z * that.z


  def norm : D =  u*u + x*x + y*y + z*z

  def abs : D = sqrt(norm)

  def real : D = u

  def imag : Vector3 = Vector3(x, y, z)

  def conjugated : Quaternion = Quaternion(u, -x, -y, -z)

  def inverse : Quaternion = Quaternion(u / norm, -x / norm, -y / norm, -z / norm)

  /**
   * Calculates and returns the normalized version of this quaternion.
   * A normalized quaternion is in the same rotation as the original quaternion,
   * but has a length of one. The normalized vector for (0,0,0) is (0,0,0).
   */
  def normalized : Quaternion = this / abs

  /**
   * True if this a quaternion with all values zero.
   */
  def isZero : Boolean = this == Quaternion.Zero

  /**
   * True if this a quaternion with u equals one and all other values zero.
   */
  def isIdentity : Boolean = this == Quaternion.One

  /**
   * Returns a four element list with the u, x, y, z values of the quaternion in that order.
   */
  def toList: List[Double] = List(u, x, y, z)

  /**
   * Returns a tuple with the x, y, z and u values of the quaternion in that order.
   */
  def toTuple : (D, D, D, D) = (u, x, y, z)

  def toEulerAngles : (D, D, D) = {
    val n = norm
    val test = x * y + z * u
    if (test > 0.49999 * n) (0, 2 * atan2(x, u), Pi/2)
    else if (test < -0.49999 * n) (0, -2*atan2(x, u), -Pi/2)
    else {
      val xx_yy = x*x - y*y
      val uu_zz = u*u - z*z
      (atan2(2 * (x*u - y*z), uu_zz - xx_yy),
       atan2(2 * (y*u - x*z), uu_zz + xx_yy),
       asin(2 * test / n))
    }
  }

  def toAngleAxis : (D, Vector3) = {
    val im = imag
    if (im == Vector3.Zero) (0, Vector3.Zero)
    else (2 * acos(u), im.normalized)
  }

  /* According to http://www.cprogramming.com/tutorial/3d/quaternions.html */
  def toMatrix : Matrix4 = {
    val f = 2 / abs
    val ux = f * u * x
    val uy = f * u * y
    val uz = f * u * z
    val xx = f * x * x
    val xy = f * x * y
    val xz = f * x * z
    val yy = f * y * y
    val yz = f * y * z
    val zz = f * z * z

    Matrix4(1 - yy - zz, xy - uz, xz + uy, 0,
	    xy + uz, 1 - xx - zz, yz - ux, 0,
	    xz - uy, yz - ux, 1 - xx - yy, 0,
	    0, 0, 0, 1)
  }

  override def toString = "Quaternion("+u+", "+x+", "+y+", "+z+")"

  override def equals(o : Any) : Boolean = o match {
      case that: Quaternion =>
	this.u == that.u && this.x == that.x && this.y == that.y && this.z == that.z
      case _ => false
    }

}
