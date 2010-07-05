package org.sgine.math

import org.sgine.math.store._

import scala.math._

import org.sgine.math.mutable.{Vector3 => MutableVector3}

trait Matrix4 extends NumericBase {
	def m00 = apply(0)
	def m01 = apply(4)
	def m02 = apply(8)
	def m03 = apply(12)
	def m10 = apply(1)
	def m11 = apply(5)
	def m12 = apply(9)
	def m13 = apply(13)
	def m20 = apply(2)
	def m21 = apply(6)
	def m22 = apply(10)
	def m23 = apply(14)
	def m30 = apply(3)
	def m31 = apply(7)
	def m32 = apply(11)
	def m33 = apply(15)
	
	def m00_=(v: Double) = {
		val n = instance
		n.store.set(0, v)
		n
	}
	def m10_=(v: Double) = {
		val n = instance
		n.store.set(1, v)
		n
	}
	def m20_=(v: Double) = {
		val n = instance
		n.store.set(2, v)
		n
	}
	def m30_=(v: Double) = {
		val n = instance
		n.store.set(3, v)
		n
	}
	def m01_=(v: Double) = {
		val n = instance
		n.store.set(4, v)
		n
	}
	def m11_=(v: Double) = {
		val n = instance
		n.store.set(5, v)
		n
	}
	def m21_=(v: Double) = {
		val n = instance
		n.store.set(6, v)
		n
	}
	def m31_=(v: Double) = {
		val n = instance
		n.store.set(7, v)
		n
	}
	def m02_=(v: Double) = {
		val n = instance
		n.store.set(8, v)
		n
	}
	def m12_=(v: Double) = {
		val n = instance
		n.store.set(9, v)
		n
	}
	def m22_=(v: Double) = {
		val n = instance
		n.store.set(10, v)
		n
	}
	def m32_=(v: Double) = {
		val n = instance
		n.store.set(11, v)
		n
	}
	def m03_=(v: Double) = {
		val n = instance
		n.store.set(12, v)
		n
	}
	def m13_=(v: Double) = {
		val n = instance
		n.store.set(13, v)
		n
	}
	def m23_=(v: Double) = {
		val n = instance
		n.store.set(14, v)
		n
	}
	def m33_=(v: Double) = {
		val n = instance
		n.store.set(15, v)
		n
	}
	
	def changeDelegate(f: () => Unit) = {
		store.changeDelegate = f
	}
	
	/**
	 * Uses this matrix to transform the specified vector.
	 * 
	 * @param v
	 * @return
	 * 		transformed Vector3
	 */
	def transform(v: Vector3): Vector3 = {
		Vector3(
				m00 * v.x + m01 * v.y + m02 * v.z + m03,
				m10 * v.x + m11 * v.y + m12 * v.z + m13,
				m20 * v.x + m21 * v.y + m22 * v.z + m23
			)
	}
	
	def transform(v: Vector3, w: Double): Vector3 = {
		Vector3(
				m00 * v.x + m01 * v.y + m02 * v.z + m03 * w,
				m10 * v.x + m11 * v.y + m12 * v.z + m13 * w,
				m20 * v.x + m21 * v.y + m22 * v.z + m23 * w
			)
	}
	
	def transformLocal(v: MutableVector3): MutableVector3 = {
		val x = m00 * v.x + m01 * v.y + m02 * v.z + m03
		val y = m10 * v.x + m11 * v.y + m12 * v.z + m13
		val z = m20 * v.x + m21 * v.y + m22 * v.z + m23
		
		v.x = x
		v.y = y
		v.z = z
		
		v
	}
	
	def transformLocal(v: MutableVector3, w: Double): MutableVector3 = {
		val x = m00 * v.x + m01 * v.y + m02 * v.z + m03 * w
		val y = m10 * v.x + m11 * v.y + m12 * v.z + m13 * w
		val z = m20 * v.x + m21 * v.y + m22 * v.z + m23 * w
		
		v.x = x
		v.y = y
		v.z = z
		
		v
	}
	
	def apply(row: Int, column: Int): Double = apply((row * 4) + column)
	
	def modify(
				m00: Double, m01: Double, m02: Double, m03: Double,
				m10: Double, m11: Double, m12: Double, m13: Double,
				m20: Double, m21: Double, m22: Double, m23: Double,
				m30: Double, m31: Double, m32: Double, m33: Double,
				f: (Double, Double) => Double) = {
		val n = instance
		
		n.store.set(0, f(this.m00, m00))
		n.store.set(1, f(this.m10, m10))
		n.store.set(2, f(this.m20, m20))
		n.store.set(3, f(this.m30, m30))
		n.store.set(4, f(this.m01, m01))
		n.store.set(5, f(this.m11, m11))
		n.store.set(6, f(this.m21, m21))
		n.store.set(7, f(this.m31, m31))
		n.store.set(8, f(this.m02, m02))
		n.store.set(9, f(this.m12, m12))
		n.store.set(10, f(this.m22, m22))
		n.store.set(11, f(this.m32, m32))
		n.store.set(12, f(this.m03, m03))
		n.store.set(13, f(this.m13, m13))
		n.store.set(14, f(this.m23, m23))
		n.store.set(15, f(this.m33, m33))
		
		n
	}
	
	def set(m00: Double = 1.0, m01: Double = 0.0, m02: Double = 0.0, m03: Double = 0.0,
		    m10: Double = 0.0, m11: Double = 1.0, m12: Double = 0.0, m13: Double = 0.0,
		    m20: Double = 0.0, m21: Double = 0.0, m22: Double = 1.0, m23: Double = 0.0,
		    m30: Double = 0.0, m31: Double = 0.0, m32: Double = 0.0, m33: Double = 1.0) = {
		val n = instance
		
		n.store.set(0, m00)
		n.store.set(1, m10)
		n.store.set(2, m20)
		n.store.set(3, m30)
		n.store.set(4, m01)
		n.store.set(5, m11)
		n.store.set(6, m21)
		n.store.set(7, m31)
		n.store.set(8, m02)
		n.store.set(9, m12)
		n.store.set(10, m22)
		n.store.set(11, m32)
		n.store.set(12, m03)
		n.store.set(13, m13)
		n.store.set(14, m23)
		n.store.set(15, m33)
		
		n.asInstanceOf[Matrix4]
	}
	
	def set(m: Matrix4): Matrix4 = {
		set(m.m00, m.m01, m.m02, m.m03,
			m.m10, m.m11, m.m12, m.m13,
			m.m20, m.m21, m.m22, m.m23,
			m.m30, m.m31, m.m32, m.m33)
	}
	
	def rotate(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = {
		val cx = cos(x)
		val sx = sin(x)
		val cy = cos(-y)
		val sy = sin(-y)
		val cz = cos(z)
		val sz = sin(z)
		
		val cxsy = cx * sy
		val sxsy = sx * sy
		
		val d00 = cy * cz
		val d01 = -cy * sz
		val d02 = -sy
		
		val d10 = -sxsy * cz + cx * sz
		val d11 = sxsy * sz + cx * cz
		val d12 = -sx * cy
		
		val d20 = cxsy * cz + sx * sz
		val d21 = -cxsy * sz + sx * cz
		val d22 = cx * cy
		
		val t00 = m00 * d00 + m01 * d10 + m02 * d20
		val t01 = m00 * d01 + m01 * d11 + m02 * d21;
		val t02 = m00 * d02 + m01 * d12 + m02 * d22;

		val t10 = m10 * d00 + m11 * d10 + m12 * d20;
		val t11 = m10 * d01 + m11 * d11 + m12 * d21;
		val t12 = m10 * d02 + m11 * d12 + m12 * d22;

		val t20 = m20 * d00 + m21 * d10 + m22 * d20;
		val t21 = m20 * d01 + m21 * d11 + m22 * d21;
		val t22 = m20 * d02 + m21 * d12 + m22 * d22;

		val t30 = m30 * d00 + m31 * d10 + m32 * d20;
		val t31 = m30 * d01 + m31 * d11 + m32 * d21;
		val t32 = m30 * d02 + m31 * d12 + m32 * d22;
		
		set(t00, t01, t02, m03,
		    t10, t11, t12, m13,
		    t20, t21, t22, m23,
		    t30, t31, t32, m33)
	}
	
	def scaleAll(v: Double): Matrix4 = scale(v, v, v)
	
	def scale(x: Double = 1.0, y: Double = 1.0, z: Double = 1.0, w: Double = 1.0) = {
		set(m00 * x, m01 * y, m02 * z, m03,
			m10 * x, m11 * y, m12 * z, m13,
			m20 * x, m21 * y, m22 * z, m23,
			m30 * x, m31 * y, m32 * z, m33)
	}
	
	def translate(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = {
		val t03 = m03 + m00 * x + m01 * y + m02 * z
	    val t13 = m13 + m10 * x + m11 * y + m12 * z
	    val t23 = m23 + m20 * x + m21 * y + m22 * z
	    val t33 = m33 + m30 * x + m31 * y + m32 * z
	    
	    set(m00, m01, m02, t03,
		    m10, m11, m12, t13,
		    m20, m21, m22, t23,
		    m30, m31, m32, t33)
	}
	
	def invert() = {
		val det = determinant()

		if (det != 0.0) {
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
		    
		    set(t00 * inv, t10 * inv, t20 * inv, t30 * inv,
		             t01 * inv, t11 * inv, t21 * inv, t31 * inv,
		             t02 * inv, t12 * inv, t22 * inv, t32 * inv,
		             t03 * inv, t13 * inv, t23 * inv, t33 * inv)
		} else {
			this
		}
	}
	
	def determinant() = {
		var d = 0.0
	    d += m00 * ( (m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
	                -(m13 * m22 * m31 + m11 * m23 * m32 + m12 * m21 * m33))
	    d -= m01 * ( (m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
	                -(m13 * m22 * m30 + m10 * m23 * m32 + m12 * m20 * m33))
	    d += m02 * ( (m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
	                -(m13 * m21 * m30 + m10 * m23 * m31 + m11 * m20 * m33))
	    d -= m03 * ( (m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
	                -(m12 * m21 * m30 + m10 * m22 * m31 + m11 * m20 * m32))
	    d
	}
	
	def determinant3x3(t00: Double, t01: Double, t02: Double,
					   t10: Double, t11: Double, t12: Double,
					   t20: Double, t21: Double, t22: Double): Double = {
	    t00 * (t11 * t22 - t12 * t21) +
	    t01 * (t12 * t20 - t10 * t22) +
	    t02 * (t10 * t21 - t11 * t20)
	}
	
	def mult(m: Matrix4) = {
		val t00 = m00 * m.m00 + m01 * m.m10 + m02 * m.m20 + m03 * m.m30
	    val t01 = m00 * m.m01 + m01 * m.m11 + m02 * m.m21 + m03 * m.m31
	    val t02 = m00 * m.m02 + m01 * m.m12 + m02 * m.m22 + m03 * m.m32
	    val t03 = m00 * m.m03 + m01 * m.m13 + m02 * m.m23 + m03 * m.m33
	
	    val t10 = m10 * m.m00 + m11 * m.m10 + m12 * m.m20 + m13 * m.m30
	    val t11 = m10 * m.m01 + m11 * m.m11 + m12 * m.m21 + m13 * m.m31
	    val t12 = m10 * m.m02 + m11 * m.m12 + m12 * m.m22 + m13 * m.m32
	    val t13 = m10 * m.m03 + m11 * m.m13 + m12 * m.m23 + m13 * m.m33
	
	    val t20 = m20 * m.m00 + m21 * m.m10 + m22 * m.m20 + m23 * m.m30
	    val t21 = m20 * m.m01 + m21 * m.m11 + m22 * m.m21 + m23 * m.m31
	    val t22 = m20 * m.m02 + m21 * m.m12 + m22 * m.m22 + m23 * m.m32
	    val t23 = m20 * m.m03 + m21 * m.m13 + m22 * m.m23 + m23 * m.m33
	
	    val t30 = m30 * m.m00 + m31 * m.m10 + m32 * m.m20 + m33 * m.m30
	    val t31 = m30 * m.m01 + m31 * m.m11 + m32 * m.m21 + m33 * m.m31
	    val t32 = m30 * m.m02 + m31 * m.m12 + m32 * m.m22 + m33 * m.m32
	    val t33 = m30 * m.m03 + m31 * m.m13 + m32 * m.m23 + m33 * m.m33
	    
	    set(t00, t01, t02, t03,
		    t10, t11, t12, t13,
		    t20, t21, t22, t23,
		    t30, t31, t32, t33)
	}
	
	def identity() = set()
	
	def buffer = store.asInstanceOf[BufferStore].buffer
	
	override def toString() = {
	    val buffer = new StringBuilder()
	
	    val columnSeparator = "  "
	    val rowSeparator = "\n"
	    val indent = "         "
	
	    def appendRow(d0: Double, d1: Double, d2: Double, d3: Double) {
	    	def appendValue(d: Double, s: String) {
	    		buffer.append(String.format("%5.02f", d.asInstanceOf[java.lang.Double]))
	    		buffer.append(s)
	    	}
	
		    buffer.append(indent)
		    appendValue(d0, columnSeparator)
		    appendValue(d1, columnSeparator)
		    appendValue(d2, columnSeparator)
		    appendValue(d3, rowSeparator)
	    }
	
	    buffer.append("Matrix4(\n")
	    appendRow(m00, m01, m02, m03)
	    appendRow(m10, m11, m12, m13)
	    appendRow(m20, m21, m22, m23)
	    appendRow(m30, m31, m32, m33)
	    buffer.append(")")
	
	    buffer.toString()
	}
}

object Matrix4 {
	val Zero = apply(m00 = 0.0, m11 = 0.0, m22 = 0.0, m33 = 0.0, mutability = Mutability.Immutable)
	val Identity = apply(mutability = Mutability.Immutable)
	
	def apply(m00: Double = 1.0, m01: Double = 0.0, m02: Double = 0.0, m03: Double = 0.0,
			  m10: Double = 0.0, m11: Double = 1.0, m12: Double = 0.0, m13: Double = 0.0,
			  m20: Double = 0.0, m21: Double = 0.0, m22: Double = 1.0, m23: Double = 0.0,
			  m30: Double = 0.0, m31: Double = 0.0, m32: Double = 0.0, m33: Double = 1.0,
			  mutability: Mutability = Mutability.Immutable, storeType: StoreType = StoreType.Default) = {
		val m = new BasicMatrix4(storeType.create(16), mutability)
		m.store.set(0, m00)
		m.store.set(1, m10)
		m.store.set(2, m20)
		m.store.set(3, m30)
		m.store.set(4, m01)
		m.store.set(5, m11)
		m.store.set(6, m21)
		m.store.set(7, m31)
		m.store.set(8, m02)
		m.store.set(9, m12)
		m.store.set(10, m22)
		m.store.set(11, m32)
		m.store.set(12, m03)
		m.store.set(13, m13)
		m.store.set(14, m23)
		m.store.set(15, m33)
		m
	}
}

class BasicMatrix4(protected[math] val store: NumericStore, val mutability: Mutability) extends Matrix4 {
	def copy() = new BasicMatrix4(store.copy(), mutability)
}