package com.sgine.math

import java.nio.DoubleBuffer
import Matrix4.D

object Matrix4{

  type D = Double

  val Zero     = new Matrix4( 0, 0, 0, 0,
                              0, 0, 0, 0,
                              0, 0, 0, 0,
                              0, 0, 0, 0 )

  val Identity = new Matrix4( 1, 0, 0, 0,
                              0, 1, 0, 0,
                              0, 0, 1, 0,
                              0, 0, 0, 1 )

  /**
   * Creates a new Matrix4 configured as an Identity matrix.
   */
  def apply() : Matrix4 = Identity
}


/**
 * An immutable Matrix4 implementation.
 */
case class Matrix4(
        m00 : D, m01 : D, m02 : D, m03 : D,
        m10 : D, m11 : D, m12 : D, m13 : D, 
        m20 : D, m21 : D, m22 : D, m23 : D,
        m30 : D, m31 : D, m32 : D, m33 : D ) {

  def this() {
    this(  1, 0, 0, 0,
           0, 1, 0, 0,
           0, 0, 1, 0,
           0, 0, 0, 1 )
  }

  // ------------------------------------------------
  // Transform

  /**
   * Uses this matrix to transform the specified vector.
   */
  def transform( v : Vector3 ) : Vector3 = {
    new Vector3(
      m00 * v.x + m01 * v.y + m02 * v.z + m03,
      m10 * v.x + m11 * v.y + m12 * v.z + m13,
      m20 * v.x + m21 * v.y + m22 * v.z + m23 )
  }


  def transform(src : DoubleBuffer , dst : DoubleBuffer ) {
    transform(src, dst, 0, src.capacity() / 3)
  }

  def transform(src : DoubleBuffer , dst : DoubleBuffer, offset : Int, length : Int) {
    if (src.position() != 0 || src.limit() != src.capacity()) throw new IllegalStateException("Src-floatbuffer must be cleared")
    if (dst.position() != 0 || dst.limit() != dst.capacity()) throw new IllegalStateException("Dst-floatbuffer must be cleared")

    // NOTE: There was a partially unrolled version of this algorithm (did 4 transformations at a time),
    // but it was actually slightly slower than this simple loop version, so it was removed.

    val off = offset * 3
    val len = length * 3

    var x = 0.0
    var y = 0.0
    var z = 0.0
    var a = off
    var b = 0
    var c = 0
    var end = off + len
    var end4 = ((end / 3) / 4 * 4) * 3

    while (a < end) {
      b = a + 1
      c = a + 2
      x = src.get(a)
      y = src.get(b)
      z = src.get(c)
      dst.put(a, m00 * x + m01 * y + m02 * z + m03)
      dst.put(b, m10 * x + m11 * y + m12 * z + m13)
      dst.put(c, m20 * x + m21 * y + m22 * z + m23)
      a += 3
    }
  }

  // ------------------------------------------------
  // Get

  def getColumn( column  : Int ) : Vector4 = {
    column match {
      case 0 => Vector4( m00, m10, m20, m30 )
      case 1 => Vector4( m01, m11, m21, m31 )
      case 2 => Vector4( m02, m12, m22, m32 )
      case 3 => Vector4( m03, m13, m23, m33 )
    }
  }

  def getRow( row : Int ) : Vector4 = {
    row match {
      case 0 => Vector4( m00, m01, m02, m03 )
      case 1 => Vector4( m10, m11, m12, m13 )
      case 2 => Vector4( m20, m21, m22, m23 )
      case 3 => Vector4( m30, m31, m32, m33 )
    }
  }

  // ------------------------------------------------
  // Scale

  def scale( s : D ) : Matrix4 = {
    new Matrix4( m00*s, m01*s, m02*s, m03,
                 m10*s, m11*s, m12*s, m13,
                 m20*s, m21*s, m22*s, m23,
                 m30*s, m31*s, m32*s, m33 )
  }

  def scale( v : Vector3 ) : Matrix4 = {
    scale( v.x, v.y, v.z )
  }

  def scale( x : D, y : D, z : D ) : Matrix4 = {
    new Matrix4( m00*x, m01*y, m02*z, m03,
                 m10*x, m11*y, m12*z, m13,
                 m20*x, m21*y, m22*z, m23,
                 m30*x, m31*y, m32*z, m33 )
  }

  def scaleX( s : D ) : Matrix4 = {
    new Matrix4( m00*s, m01, m02, m03,
                 m10*s, m11, m12, m13,
                 m20*s, m21, m22, m23,
                 m30*s, m31, m32, m33 )
  }

  def scaleY( s : D ) : Matrix4 = {
    new Matrix4( m00, m01*s, m02, m03,
                 m10, m11*s, m12, m13,
                 m20, m21*s, m22, m23,
                 m30, m31*s, m32, m33 )
  }

  def scaleZ( s : D ) : Matrix4 = {
    new Matrix4( m00, m01, m02*s, m03,
                 m10, m11, m12*s, m13,
                 m20, m21, m22*s, m23,
                 m30, m31, m32*s, m33 )
  }

  // ------------------------------------------------
  // Transpose

  def transpose() : Matrix4 = {
    val t01 = m10;
    val t02 = m20;
    val t03 = m30;

    val t10 = m01;
    val t12 = m21;
    val t13 = m31;

    val t20 = m02;
    val t21 = m12;
    val t23 = m32;

    val t30 = m03;
    val t31 = m13;
    val t32 = m23;

    return Matrix4( m00, t01, t02, t03,
                    t10, m11, t12, t13,
                    t20, t21, m22, t23,
                    t30, t31, t32, m33 )
  }



  // ------------------------------------------------
  // Invert

  def invert : Matrix4 = {
    val det = determinant()

    if (det == 0.0) {
      return this
    }

    // first row
    val t00 = +determinant3x3(m11, m12, m13, m21, m22, m23, m31, m32, m33)
    val t01 = -determinant3x3(m10, m12, m13, m20, m22, m23, m30, m32, m33)
    val t02 = +determinant3x3(m10, m11, m13, m20, m21, m23, m30, m31, m33)
    val t03 = -determinant3x3(m10, m11, m12, m20, m21, m22, m30, m31, m32)
    // second row
    val t10 = -determinant3x3(m01, m02, m03, m21, m22, m23, m31, m32, m33)
    val t11 = +determinant3x3(m00, m02, m03, m20, m22, m23, m30, m32, m33)
    val t12 = -determinant3x3(m00, m01, m03, m20, m21, m23, m30, m31, m33)
    val t13 = +determinant3x3(m00, m01, m02, m20, m21, m22, m30, m31, m32)
    // third row
    val t20 = +determinant3x3(m01, m02, m03, m11, m12, m13, m31, m32, m33)
    val t21 = -determinant3x3(m00, m02, m03, m10, m12, m13, m30, m32, m33)
    val t22 = +determinant3x3(m00, m01, m03, m10, m11, m13, m30, m31, m33)
    val t23 = -determinant3x3(m00, m01, m02, m10, m11, m12, m30, m31, m32)
    // fourth row
    val t30 = -determinant3x3(m01, m02, m03, m11, m12, m13, m21, m22, m23)
    val t31 = +determinant3x3(m00, m02, m03, m10, m12, m13, m20, m22, m23)
    val t32 = -determinant3x3(m00, m01, m03, m10, m11, m13, m20, m21, m23)
    val t33 = +determinant3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22)

    // transpose and divide by the determinant
    val inv = 1.0 / det
    new Matrix4(  t00 * inv, t10 * inv, t20 * inv, t30 * inv,
                  t01 * inv, t11 * inv, t21 * inv, t31 * inv,
                  t02 * inv, t12 * inv, t22 * inv, t32 * inv,
                  t03 * inv, t13 * inv, t23 * inv, t33 * inv )
  }

  def determinant : Double = {
    val d = 0.0
    d += m00 * (   (m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
                 - (m13 * m22 * m31 + m11 * m23 * m32 + m12 * m21 * m33) )
    d -= m01 * (   (m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
                 - (m13 * m22 * m30 + m10 * m23 * m32 + m12 * m20 * m33) )
    d += m02 * (   (m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
                 - (m13 * m21 * m30 + m10 * m23 * m31 + m11 * m20 * m33) )
    d -= m03 * (   (m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
                 - (m12 * m21 * m30 + m10 * m22 * m31 + m11 * m20 * m32) )
    d
  }

  private def determinant3x3( t00 : D, t01 : D, t02 : D,
                      t10 : D, t11 : D, t12 : D,
                      t20 : D, t21 : D, t22 : D) : Double = {
    t00 * (t11 * t22 - t12 * t21) +
    t01 * (t12 * t20 - t10 * t22) +
    t02 * (t10 * t21 - t11 * t20)
  }



  // ------------------------------------------------
  // Rotate

  def rotX( a : D ) : Matrix4 = {
    // TODO: Check that the unit is correct - should it be radians or Degrees
    val d11 = Math.cos( a ) // Was:  FastMath.cosDeg(a)
    val d21 = Math.sin( a ) // Was:  FastMath.sinDeg(a)
    val d12 = -d21
    val d22 = d11

    val t01 = m01 * d11 + m02 * d21
    val t02 = m01 * d12 + m02 * d22
    val t11 = m11 * d11 + m12 * d21
    val t12 = m11 * d12 + m12 * d22
    val t21 = m21 * d11 + m22 * d21
    val t22 = m21 * d12 + m22 * d22
    val t31 = m31 * d11 + m32 * d21
    val t32 = m31 * d12 + m32 * d22

    new Matrix4( m00, t01, t02, m03,
                 m10, t11, t12, m13,
                 m20, t21, t22, m23,
                 m30, t31, t32, m33 )
  }

  def rotY( a : D ) : Matrix4 = {
    val d00 = Math.cos( a )
    val d20 = Math.sin( a )
    val d02 = -d20
    val d22 = d00

    val t00 = m00 * d00 + m02 * d20;
    val t02 = m00 * d02 + m02 * d22;
    val t10 = m10 * d00 + m12 * d20;
    val t12 = m10 * d02 + m12 * d22;
    val t20 = m20 * d00 + m22 * d20;
    val t22 = m20 * d02 + m22 * d22;
    val t30 = m30 * d00 + m32 * d20;
    val t32 = m30 * d02 + m32 * d22;

    new Matrix4( t00, m01, t02, m03,
                 t10, m11, t12, m13,
                 t20, m21, t22, m23,
                 t30, m31, t32, m33 )
  }

  def rotZ( a : D ) : Matrix4 = {
    val d00 = Math.cos( a )
    val d10 = Math.sin( a )
    val d01 = -d10
    val d11 = d00

    val t00 = m00 * d00 + m01 * d10
    val t01 = m00 * d01 + m01 * d11
    val t10 = m10 * d00 + m11 * d10
    val t11 = m10 * d01 + m11 * d11
    val t20 = m20 * d00 + m21 * d10
    val t21 = m20 * d01 + m21 * d11
    val t30 = m30 * d00 + m31 * d10
    val t31 = m30 * d01 + m31 * d11

    new Matrix4( t00, t01, m02, m03,
                 t10, t11, m12, m13,
                 t20, t21, m22, m23,
                 t30, t31, m32, m33 )
  }

  def rotate(rot : Vector3 ) : Matrix4 = {
    rotate(rot.x, rot.y, rot.z)
  }

  def rotate( x : D, y : D, z : D ) : Matrix4 = {
    y = -y

    val cx = Math.cos( x )
    val sx = Math.sin( x )
    val cy = Math.cos( y )
    val sy = Math.sin( y )
    val cz = Math.cos( z )
    val sz = Math.sin( z )

    val cxsy = cx * sy
    val sxsy = sx * sy

    val d00 = +cy * +cz
    val d01 = -cy * +sz
    val d02 = -sy

    val d10 = -sxsy * cz + cx * sz
    val d11 = +sxsy * sz + cx * cz
    val d12 = -sx * +cy

    val d20 = +cxsy * cz + sx * sz
    val d21 = -cxsy * sz + sx * cz
    val d22 = +cx * +cy

    val t00 = m00 * d00 + m01 * d10 + m02 * d20
    val t01 = m00 * d01 + m01 * d11 + m02 * d21
    val t02 = m00 * d02 + m01 * d12 + m02 * d22

    val t10 = m10 * d00 + m11 * d10 + m12 * d20
    val t11 = m10 * d01 + m11 * d11 + m12 * d21
    val t12 = m10 * d02 + m11 * d12 + m12 * d22

    val t20 = m20 * d00 + m21 * d10 + m22 * d20
    val t21 = m20 * d01 + m21 * d11 + m22 * d21
    val t22 = m20 * d02 + m21 * d12 + m22 * d22

    val t30 = m30 * d00 + m31 * d10 + m32 * d20
    val t31 = m30 * d01 + m31 * d11 + m32 * d21
    val t32 = m30 * d02 + m31 * d12 + m32 * d22

    new Matrix4( t00, t01, t02, m03,
                 t10, t11, t12, m13,
                 t20, t21, t22, m23,
                 t30, t31, t32, m33 )
  }



  // TODO:

  // Transform array with 3d coordinates
  // Scale
  // Translate
  // Invert
  // Determinant
  // Multiply
  // To/from array
  // To/(from?) string

}



/* Old code from jseamless:

/*
 * Created on Sep 21, 2004
 */
package org.jseamless.math;

import java.nio.FloatBuffer;
import java.text.NumberFormat;
import java.util.Arrays;

public class Matrix4f {
	private static final NumberFormat format = NumberFormat.getInstance();
	static {
		format.setMinimumFractionDigits(3);
		format.setMaximumFractionDigits(3);
		format.setMinimumIntegerDigits(3);
		format.setMaximumIntegerDigits(3);
	}

	private Matrix4f[] stack;
	private int stackPosition;

	public float m00, m01, m02, m03;
	public float m10, m11, m12, m13;
	public float m20, m21, m22, m23;
	public float m30, m31, m32, m33;

	public Matrix4f() {
		stackPosition = -1;
		m00 = m11 = m22 = m33 = 1.0F;
	}

	public void push() {
		stackPosition++;
		if (stack == null) {
			stack = new Matrix4f[1];
			stack[stackPosition] = new Matrix4f();
		} else if (stackPosition == stack.length) {
			stack = Arrays.copyOf(stack, stack.length + 1);
			stack[stackPosition] = new Matrix4f();
		}
		stack[stackPosition].set(this);
	}

	public void pop() {
		set(stack[stackPosition]);
		stackPosition--;
	}

	/**
	 * PROCESS
	 */

	public final Vector3f fillOrigin(Vector3f vec) {
		vec.x = m03;
		vec.y = m13;
		vec.z = m23;

		return vec;
	}


	public final void transform(FloatBuffer src, FloatBuffer dst) {
		this.transform(src, dst, 0, src.capacity() / 3);
	}

	public final void transform(FloatBuffer src, FloatBuffer dst, int off,
			int len) {
		if (src.position() != 0 || src.limit() != src.capacity())
			throw new IllegalStateException("Src-floatbuffer must be cleared");
		if (dst.position() != 0 || dst.limit() != dst.capacity())
			throw new IllegalStateException("Dst-floatbuffer must be cleared");

		off *= 3;
		len *= 3;

		float x, y, z;
		int a = off, b, c;
		int end = off + len;
		int end4 = ((end / 3) / 4 * 4) * 3;

		while (a < end4) {
			b = a + 1;
			c = a + 2;
			x = src.get(a);
			y = src.get(b);
			z = src.get(c);
			dst.put(a, m00 * x + m01 * y + m02 * z + m03);
			dst.put(b, m10 * x + m11 * y + m12 * z + m13);
			dst.put(c, m20 * x + m21 * y + m22 * z + m23);
			a += 3;

			b = a + 1;
			c = a + 2;
			x = src.get(a);
			y = src.get(b);
			z = src.get(c);
			dst.put(a, m00 * x + m01 * y + m02 * z + m03);
			dst.put(b, m10 * x + m11 * y + m12 * z + m13);
			dst.put(c, m20 * x + m21 * y + m22 * z + m23);
			a += 3;

			b = a + 1;
			c = a + 2;
			x = src.get(a);
			y = src.get(b);
			z = src.get(c);
			dst.put(a, m00 * x + m01 * y + m02 * z + m03);
			dst.put(b, m10 * x + m11 * y + m12 * z + m13);
			dst.put(c, m20 * x + m21 * y + m22 * z + m23);
			a += 3;

			b = a + 1;
			c = a + 2;
			x = src.get(a);
			y = src.get(b);
			z = src.get(c);
			dst.put(a, m00 * x + m01 * y + m02 * z + m03);
			dst.put(b, m10 * x + m11 * y + m12 * z + m13);
			dst.put(c, m20 * x + m21 * y + m22 * z + m23);
			a += 3;
		}

		while (a < end) {
			b = a + 1;
			c = a + 2;
			x = src.get(a);
			y = src.get(b);
			z = src.get(c);
			dst.put(a, m00 * x + m01 * y + m02 * z + m03);
			dst.put(b, m10 * x + m11 * y + m12 * z + m13);
			dst.put(c, m20 * x + m21 * y + m22 * z + m23);
			a += 3;
		}
	}

	public final void transform(float[] src, float[] dst) {
		this.transform(src, dst, 0, src.length / 3);
	}

	public final void transform(float[] src, float[] dst, int off, int len) {
		off *= 3;
		len *= 3;

		float x, y, z;
		int a = off, b, c;
		int end = off + len;
		int end4 = ((end / 3) / 4 * 4) * 3;

		while (a < end4) {
			b = a + 1;
			c = a + 2;
			x = src[a];
			y = src[b];
			z = src[c];
			dst[a] = m00 * x + m01 * y + m02 * z + m03;
			dst[b] = m10 * x + m11 * y + m12 * z + m13;
			dst[c] = m20 * x + m21 * y + m22 * z + m23;
			a += 3;

			b = a + 1;
			c = a + 2;
			x = src[a];
			y = src[b];
			z = src[c];
			dst[a] = m00 * x + m01 * y + m02 * z + m03;
			dst[b] = m10 * x + m11 * y + m12 * z + m13;
			dst[c] = m20 * x + m21 * y + m22 * z + m23;
			a += 3;

			b = a + 1;
			c = a + 2;
			x = src[a];
			y = src[b];
			z = src[c];
			dst[a] = m00 * x + m01 * y + m02 * z + m03;
			dst[b] = m10 * x + m11 * y + m12 * z + m13;
			dst[c] = m20 * x + m21 * y + m22 * z + m23;
			a += 3;

			b = a + 1;
			c = a + 2;
			x = src[a];
			y = src[b];
			z = src[c];
			dst[a] = m00 * x + m01 * y + m02 * z + m03;
			dst[b] = m10 * x + m11 * y + m12 * z + m13;
			dst[c] = m20 * x + m21 * y + m22 * z + m23;
			a += 3;
		}

		while (a < end) {
			b = a + 1;
			c = a + 2;
			x = src[a];
			y = src[b];
			z = src[c];
			dst[a] = m00 * x + m01 * y + m02 * z + m03;
			dst[b] = m10 * x + m11 * y + m12 * z + m13;
			dst[c] = m20 * x + m21 * y + m22 * z + m23;
			a += 3;
		}
	}

	/**
	 * TRANSLATION
	 */

	public final void getTranslation(Vector3f translation) {
		translation.load(m03, m13, m23);
	}

	/**
	 * COLUMN
	 */

	public final void setColumn(int column, Vector4f v) {
		switch (column) {
		case 0:
			m00 = v.x;
			m10 = v.y;
			m20 = v.z;
			m30 = v.w;
			break;
		case 1:
			m01 = v.x;
			m11 = v.y;
			m21 = v.z;
			m31 = v.w;
			break;
		case 2:
			m02 = v.x;
			m12 = v.y;
			m22 = v.z;
			m32 = v.w;
			break;
		case 3:
			m03 = v.x;
			m13 = v.y;
			m23 = v.z;
			m33 = v.w;
			break;
		}
	}

	public final void getColumn(int column, Vector4f v) {
		switch (column) {
		case 0:
			v.x = m00;
			v.y = m10;
			v.z = m20;
			v.w = m30;
			break;
		case 1:
			v.x = m01;
			v.y = m11;
			v.z = m21;
			v.w = m31;
			break;
		case 2:
			v.x = m02;
			v.y = m12;
			v.z = m22;
			v.w = m32;
			break;
		case 3:
			v.x = m03;
			v.y = m13;
			v.z = m23;
			v.w = m33;
			break;
		}
	}

	/**
	 * ROW
	 */

	public final void setRow(int row, Vector4f v) {
		switch (row) {
		case 0:
			m00 = v.x();
			m01 = v.y();
			m02 = v.z();
			m03 = v.w();
			break;
		case 1:
			m10 = v.x();
			m11 = v.y();
			m12 = v.z();
			m13 = v.w();
			break;
		case 2:
			m20 = v.x();
			m21 = v.y();
			m22 = v.z();
			m23 = v.w();
			break;
		case 3:
			m30 = v.x();
			m31 = v.y();
			m32 = v.z();
			m33 = v.w();
			break;
		}
	}

	public final void getRow(int row, Vector4f v) {
		switch (row) {
		case 0:
			v.x(m00);
			v.y(m01);
			v.z(m02);
			v.w(m03);
			break;
		case 1:
			v.x(m10);
			v.y(m11);
			v.z(m12);
			v.w(m13);
			break;
		case 2:
			v.x(m20);
			v.y(m21);
			v.z(m22);
			v.w(m23);
			break;
		case 3:
			v.x(m30);
			v.y(m31);
			v.z(m32);
			v.w(m33);
			break;
		}
	}

	/**
	 * GET / SET
	 */

	public final void get(FloatBuffer dst) {
		int pos = dst.position();

		dst.put(pos + 0, m00);
		dst.put(pos + 1, m01);
		dst.put(pos + 2, m02);
		dst.put(pos + 3, m03);

		dst.put(pos + 4, m10);
		dst.put(pos + 5, m11);
		dst.put(pos + 6, m12);
		dst.put(pos + 7, m13);

		dst.put(pos + 8, m20);
		dst.put(pos + 9, m21);
		dst.put(pos + 10, m22);
		dst.put(pos + 11, m23);

		dst.put(pos + 12, m30);
		dst.put(pos + 13, m31);
		dst.put(pos + 14, m32);
		dst.put(pos + 15, m33);

		dst.position(pos + 16);
	}

	public final void get(Matrix4f dst) {
		dst.m00 = m00;
		dst.m01 = m01;
		dst.m02 = m02;
		dst.m03 = m03;

		dst.m10 = m10;
		dst.m11 = m11;
		dst.m12 = m12;
		dst.m13 = m13;

		dst.m20 = m20;
		dst.m21 = m21;
		dst.m22 = m22;
		dst.m23 = m23;

		dst.m30 = m30;
		dst.m31 = m31;
		dst.m32 = m32;
		dst.m33 = m33;
	}

	public final void set(Matrix4f src) {
		m00 = src.m00;
		m01 = src.m01;
		m02 = src.m02;
		m03 = src.m03;

		m10 = src.m10;
		m11 = src.m11;
		m12 = src.m12;
		m13 = src.m13;

		m20 = src.m20;
		m21 = src.m21;
		m22 = src.m22;
		m23 = src.m23;

		m30 = src.m30;
		m31 = src.m31;
		m32 = src.m32;
		m33 = src.m33;
	}

	public final void set(FloatBuffer src) {
		int pos = src.position();

		m00 = src.get(pos + 0);
		m01 = src.get(pos + 1);
		m02 = src.get(pos + 2);
		m03 = src.get(pos + 3);

		m10 = src.get(pos + 4);
		m11 = src.get(pos + 5);
		m12 = src.get(pos + 6);
		m13 = src.get(pos + 7);

		m20 = src.get(pos + 8);
		m21 = src.get(pos + 9);
		m22 = src.get(pos + 10);
		m23 = src.get(pos + 11);

		m30 = src.get(pos + 12);
		m31 = src.get(pos + 13);
		m32 = src.get(pos + 14);
		m33 = src.get(pos + 15);

		src.position(pos + 16);
	}

	/**
	 * GET / SET TRANSPOSE
	 */

	public final void getTranspose(FloatBuffer dst) {
		int pos = dst.position();

		dst.put(pos + 0, m00);
		dst.put(pos + 1, m10);
		dst.put(pos + 2, m20);
		dst.put(pos + 3, m30);

		dst.put(pos + 4, m01);
		dst.put(pos + 5, m11);
		dst.put(pos + 6, m21);
		dst.put(pos + 7, m31);

		dst.put(pos + 8, m02);
		dst.put(pos + 9, m12);
		dst.put(pos + 10, m22);
		dst.put(pos + 11, m32);

		dst.put(pos + 12, m03);
		dst.put(pos + 13, m13);
		dst.put(pos + 14, m23);
		dst.put(pos + 15, m33);

		dst.position(pos + 16);
	}

	public final void getTranspose(Matrix4f dst) {
		dst.m00 = m00;
		dst.m01 = m10;
		dst.m02 = m20;
		dst.m03 = m30;

		dst.m10 = m01;
		dst.m11 = m11;
		dst.m12 = m21;
		dst.m13 = m31;

		dst.m20 = m02;
		dst.m21 = m12;
		dst.m22 = m22;
		dst.m23 = m32;

		dst.m30 = m03;
		dst.m31 = m13;
		dst.m32 = m23;
		dst.m33 = m33;
	}

	public final void setTranspose(Matrix4f src) {
		m00 = src.m00;
		m01 = src.m10;
		m02 = src.m20;
		m03 = src.m30;

		m10 = src.m01;
		m11 = src.m11;
		m12 = src.m21;
		m13 = src.m31;

		m20 = src.m02;
		m21 = src.m12;
		m22 = src.m22;
		m23 = src.m32;

		m30 = src.m03;
		m31 = src.m13;
		m32 = src.m23;
		m33 = src.m33;
	}

	public final void setTranspose(FloatBuffer src) {
		int pos = src.position();

		m00 = src.get(pos + 0);
		m10 = src.get(pos + 1);
		m20 = src.get(pos + 2);
		m30 = src.get(pos + 3);

		m01 = src.get(pos + 4);
		m11 = src.get(pos + 5);
		m21 = src.get(pos + 6);
		m31 = src.get(pos + 7);

		m02 = src.get(pos + 8);
		m12 = src.get(pos + 9);
		m22 = src.get(pos + 10);
		m32 = src.get(pos + 11);

		m03 = src.get(pos + 12);
		m13 = src.get(pos + 13);
		m23 = src.get(pos + 14);
		m33 = src.get(pos + 15);

		src.position(pos + 16);
	}

	/**
	 * IDENTITY
	 */

	public final Matrix4f identity() {
		m01 = m02 = m03 = 0.0f;
		m10 = m12 = m13 = 0.0f;
		m20 = m21 = m23 = 0.0f;
		m30 = m31 = m32 = 0.0f;
		m00 = m11 = m22 = m33 = 1.0f;
		return this;
	}

	/**
	 * TRANSLATE
	 */

	public final Matrix4f translate(Vector3f v) {
		return this.translate(v.x(), v.y(), v.z());
	}

	public final Matrix4f translate(float x, float y, float z) {
		m03 += m00 * x + m01 * y + m02 * z;
		m13 += m10 * x + m11 * y + m12 * z;
		m23 += m20 * x + m21 * y + m22 * z;
		m33 += m30 * x + m31 * y + m32 * z;

		return this;
	}

	public final Matrix4f translateX(float t) {
		m03 += m00 * t;
		m13 += m10 * t;
		m23 += m20 * t;
		m33 += m30 * t;

		return this;
	}

	public final Matrix4f translateY(float t) {
		m03 += m01 * t;
		m13 += m11 * t;
		m23 += m21 * t;
		m33 += m31 * t;

		return this;
	}

	public final Matrix4f translateZ(float t) {
		m03 += m02 * t;
		m13 += m12 * t;
		m23 += m22 * t;
		m33 += m32 * t;

		return this;
	}

	/**
	 * MULT
	 */

	public final Matrix4f mult(Matrix4f m) {
		float t00 = m00 * m.m00 + m01 * m.m10 + m02 * m.m20 + m03 * m.m30;
		float t01 = m00 * m.m01 + m01 * m.m11 + m02 * m.m21 + m03 * m.m31;
		float t02 = m00 * m.m02 + m01 * m.m12 + m02 * m.m22 + m03 * m.m32;
		float t03 = m00 * m.m03 + m01 * m.m13 + m02 * m.m23 + m03 * m.m33;

		float t10 = m10 * m.m00 + m11 * m.m10 + m12 * m.m20 + m13 * m.m30;
		float t11 = m10 * m.m01 + m11 * m.m11 + m12 * m.m21 + m13 * m.m31;
		float t12 = m10 * m.m02 + m11 * m.m12 + m12 * m.m22 + m13 * m.m32;
		float t13 = m10 * m.m03 + m11 * m.m13 + m12 * m.m23 + m13 * m.m33;

		float t20 = m20 * m.m00 + m21 * m.m10 + m22 * m.m20 + m23 * m.m30;
		float t21 = m20 * m.m01 + m21 * m.m11 + m22 * m.m21 + m23 * m.m31;
		float t22 = m20 * m.m02 + m21 * m.m12 + m22 * m.m22 + m23 * m.m32;
		float t23 = m20 * m.m03 + m21 * m.m13 + m22 * m.m23 + m23 * m.m33;

		float t30 = m30 * m.m00 + m31 * m.m10 + m32 * m.m20 + m33 * m.m30;
		float t31 = m30 * m.m01 + m31 * m.m11 + m32 * m.m21 + m33 * m.m31;
		float t32 = m30 * m.m02 + m31 * m.m12 + m32 * m.m22 + m33 * m.m32;
		float t33 = m30 * m.m03 + m31 * m.m13 + m32 * m.m23 + m33 * m.m33;

		//

		m00 = t00;
		m01 = t01;
		m02 = t02;
		m03 = t03;

		m10 = t10;
		m11 = t11;
		m12 = t12;
		m13 = t13;

		m20 = t20;
		m21 = t21;
		m22 = t22;
		m23 = t23;

		m30 = t30;
		m31 = t31;
		m32 = t32;
		m33 = t33;

		return this;
	}

	/**
	 * EQUALS
	 */

	public final boolean equals(Matrix4f mat, float margin) {
		if (!(EasyMath.equals(m00, mat.m00, margin)
				&& EasyMath.equals(m10, mat.m10, margin)
				&& EasyMath.equals(m20, mat.m20, margin) && EasyMath.equals(
				m30, mat.m30, margin)))
			return false;
		if (!(EasyMath.equals(m01, mat.m01, margin)
				&& EasyMath.equals(m11, mat.m11, margin)
				&& EasyMath.equals(m21, mat.m21, margin) && EasyMath.equals(
				m31, mat.m31, margin)))
			return false;
		if (!(EasyMath.equals(m02, mat.m02, margin)
				&& EasyMath.equals(m12, mat.m12, margin)
				&& EasyMath.equals(m22, mat.m22, margin) && EasyMath.equals(
				m32, mat.m32, margin)))
			return false;
		return EasyMath.equals(m03, mat.m03, margin)
				&& EasyMath.equals(m13, mat.m13, margin)
				&& EasyMath.equals(m23, mat.m23, margin)
				&& EasyMath.equals(m33, mat.m33, margin);
	}

	public final float cumulativeDiff(Matrix4f m) {
		float sum = 0.0f;

		sum += Math.abs(m00 - m.m00);
		sum += Math.abs(m01 - m.m01);
		sum += Math.abs(m02 - m.m02);
		sum += Math.abs(m03 - m.m03);

		sum += Math.abs(m10 - m.m10);
		sum += Math.abs(m11 - m.m11);
		sum += Math.abs(m12 - m.m12);
		sum += Math.abs(m13 - m.m13);

		sum += Math.abs(m20 - m.m20);
		sum += Math.abs(m21 - m.m21);
		sum += Math.abs(m22 - m.m22);
		sum += Math.abs(m23 - m.m23);

		sum += Math.abs(m30 - m.m30);
		sum += Math.abs(m31 - m.m31);
		sum += Math.abs(m32 - m.m32);
		sum += Math.abs(m33 - m.m33);

		return sum;
	}

	public final String toString() {
		StringBuffer buffer = new StringBuffer();

		String sep = "  ";

		buffer.append("Matrix4f[ ");
		buffer.append(format.format(m00) + sep);
		buffer.append(format.format(m01) + sep);
		buffer.append(format.format(m02) + sep);
		buffer.append(format.format(m03) + "\n");

		buffer.append("         ");
		buffer.append(format.format(m10) + sep);
		buffer.append(format.format(m11) + sep);
		buffer.append(format.format(m12) + sep);
		buffer.append(format.format(m13) + "\n");

		buffer.append("         ");
		buffer.append(format.format(m20) + sep);
		buffer.append(format.format(m21) + sep);
		buffer.append(format.format(m22) + sep);
		buffer.append(format.format(m23) + "\n");

		buffer.append("         ");
		buffer.append(format.format(m30) + sep);
		buffer.append(format.format(m31) + sep);
		buffer.append(format.format(m32) + sep);
		buffer.append(format.format(m33) + " ]");

		return buffer.toString();
	}
}

*/
