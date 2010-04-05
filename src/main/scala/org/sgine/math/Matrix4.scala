package org.sgine.math

import java.nio._

import Matrix4._

/**
 * An immutable Matrix4 implementation that utilizes a backing
 * DoubleBuffer in column form for direct translation to OpenGL.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Matrix4 protected() extends Iterable[Double] {
	val b = createBuffer()
	
	def m00 = b.get(0)
	protected def m00_=(v: Double) = b.put(0, v)
	def m10 = b.get(1)
	protected def m10_=(v: Double) = b.put(1, v)
	def m20 = b.get(2)
	protected def m20_=(v: Double) = b.put(2, v)
	def m30 = b.get(3)
	protected def m30_=(v: Double) = b.put(3, v)
	def m01 = b.get(4)
	protected def m01_=(v: Double) = b.put(4, v)
	def m11 = b.get(5)
	protected def m11_=(v: Double) = b.put(5, v)
	def m21 = b.get(6)
	protected def m21_=(v: Double) = b.put(6, v)
	def m31 = b.get(7)
	protected def m31_=(v: Double) = b.put(7, v)
	def m02 = b.get(8)
	protected def m02_=(v: Double) = b.put(8, v)
	def m12 = b.get(9)
	protected def m12_=(v: Double) = b.put(9, v)
	def m22 = b.get(10)
	protected def m22_=(v: Double) = b.put(10, v)
	def m32 = b.get(11)
	protected def m32_=(v: Double) = b.put(11, v)
	def m03 = b.get(12)
	protected def m03_=(v: Double) = b.put(12, v)
	def m13 = b.get(13)
	protected def m13_=(v: Double) = b.put(13, v)
	def m23 = b.get(14)
	protected def m23_=(v: Double) = b.put(14, v)
	def m33 = b.get(15)
	protected def m33_=(v: Double) = b.put(15, v)
	
/////// Transforms ////////////
	
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
	
	def transform(src: DoubleBuffer , dst: DoubleBuffer ) {
		transform(src, dst, 0, src.capacity() / 3)
	}
	
	def transform(src: DoubleBuffer, dst: DoubleBuffer, offset: Int, length: Int) = {
		if (src.position() != 0 || src.limit() != src.capacity()) throw new IllegalStateException("Src-Doublebuffer must be cleared")
		if (dst.position() != 0 || dst.limit() != dst.capacity()) throw new IllegalStateException("Dst-Doublebuffer must be cleared")
		
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
	
/////// Get ////////////
	
	def get(row: Int, column: Int) = b.get((row * 4) + column)
	
	def getColumn(column: Int): Vector4 = Vector4(column * 4, column * 4 + 1, column * 4 + 2, column * 4 + 3)
	
	def getRow(row: Int): Vector4 = Vector4(row, row + 4, row + 8, row + 12)
	
	def toBuffer(destination: DoubleBuffer = null, updatePosition: Boolean = true) = {
		var dst = destination
		if (dst == null) dst = createBuffer()
		
		// TODO: verify that order isn't important here?
		// Original order was m00, m01, m02
		// New order is m00, m10, m20
		val pos = dst.position
		for (i <- 0 until 16) {
			dst.put(pos + i, b.get(i))
		}
		
		if (updatePosition) {
			dst.position(pos + 16)
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
	
	def cumulativeDifference(m: Matrix4): Double = {
		var sum = 0.0
		
		def addDiff(a: Double, b: Double) {
			sum += Math.abs(a - b)
		}
		
		addDiff(m00, m.m00)
		addDiff(m01, m.m01)
		addDiff(m02, m.m02)
		addDiff(m03, m.m03)
		
		addDiff(m10, m.m10)
		addDiff(m11, m.m11)
		addDiff(m12, m.m12)
		addDiff(m13, m.m13)
		
		addDiff(m20, m.m20)
		addDiff(m21, m.m21)
		addDiff(m22, m.m22)
		addDiff(m23, m.m23)
		
		addDiff(m30, m.m30)
		addDiff(m31, m.m31)
		addDiff(m32, m.m32)
		addDiff(m33, m.m33)
		
		sum
	}
	
/////// Modify ////////////
	
	def setEntry(row: Int, column: Int, v: Double) = copy().setEntryLocal(row, column, v)
	
	def setColumn(column: Int, v: Double) = copy().setColumnLocal(column, v)
	
	def setRow(row: Int, v: Double) = copy().setRowLocal(row, v)
	
	def set(
			m00: Double = Double.NaN, m01: Double = Double.NaN, m02: Double = Double.NaN, m03: Double = Double.NaN,
			m10: Double = Double.NaN, m11: Double = Double.NaN, m12: Double = Double.NaN, m13: Double = Double.NaN,
			m20: Double = Double.NaN, m21: Double = Double.NaN, m22: Double = Double.NaN, m23: Double = Double.NaN,
			m30: Double = Double.NaN, m31: Double = Double.NaN, m32: Double = Double.NaN, m33: Double = Double.NaN
		   ) = copy().setLocal(
		  		   				m00, m01, m02, m03,
		  		   				m10, m11, m12, m13,
		  		   				m20, m21, m22, m23,
		  		   				m30, m31, m32, m33
		  		   			  )
	
	def set(m: Matrix4) = copy().setLocal(m)
	
	def mult(row: Int, column: Int, m: Double) = copy().multLocal(row, column, m)
	
	def mult(m: Matrix4) = copy().multLocal(m)
	
	def multRow(row: Int, m: Double) = copy().multRowLocal(row, m)
	
	def multColumn(column: Int, m: Double) = copy().multColumnLocal(column, m)
	
	def scale(x: Double = 1.0, y: Double = 1.0, z: Double = 1.0, w: Double = 1.0) = copy().scaleLocal(x, y, z, w)
	
	def scale(v: Vector3): Matrix4 = scale(v.x, v.y, v.z)
	
	def invert() = copy().invertLocal()
	
	def rotate(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = copy().rotateLocal(x, y, z)
	
	def rotate(v: Vector3): Matrix4 = rotate(v.x, v.y, v.z)
	
	def translate(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = copy().translateLocal(x, y, z)
	
/////// Modify local ////////////
	
	protected def setEntryLocal(row: Int, column: Int, v: Double) = {
		b.put((row * 4) + column, v)
		
		this
	}
	
	protected def setColumnLocal(column: Int, v: Double*) = {
		for (i <- 0 until 4) {
			setEntryLocal(i, column, v(i))
		}
		
		this
	}
	
	protected def setRowLocal(row: Int, v: Double*) = {
		for (i <- 0 until 4) {
			setEntryLocal(row, i, v(i))
		}
		
		this
	}
	
	protected def setLocal(
				m00: Double = Double.NaN, m01: Double = Double.NaN, m02: Double = Double.NaN, m03: Double = Double.NaN,
				m10: Double = Double.NaN, m11: Double = Double.NaN, m12: Double = Double.NaN, m13: Double = Double.NaN,
				m20: Double = Double.NaN, m21: Double = Double.NaN, m22: Double = Double.NaN, m23: Double = Double.NaN,
				m30: Double = Double.NaN, m31: Double = Double.NaN, m32: Double = Double.NaN, m33: Double = Double.NaN
			) = {
		if (!m00.isNaN) this.m00 = m00
		if (!m01.isNaN) this.m01 = m01
		if (!m02.isNaN) this.m02 = m02
		if (!m03.isNaN) this.m03 = m03
		
		if (!m10.isNaN) this.m10 = m10
		if (!m11.isNaN) this.m11 = m11
		if (!m12.isNaN) this.m12 = m12
		if (!m13.isNaN) this.m13 = m13
		
		if (!m20.isNaN) this.m20 = m20
		if (!m21.isNaN) this.m21 = m21
		if (!m22.isNaN) this.m22 = m22
		if (!m23.isNaN) this.m23 = m23
		
		if (!m30.isNaN) this.m30 = m30
		if (!m31.isNaN) this.m31 = m31
		if (!m32.isNaN) this.m32 = m32
		if (!m33.isNaN) this.m33 = m33
		
		this
	}
	
	protected def setLocal(m: Matrix4) = m.toBuffer(b, false); this
	
	protected def multLocal(row: Int, column: Int, m: Double) = setEntryLocal(row, column, get(row, column) * m)
	
	protected def multRowLocal(row: Int, m: Double) = {
		for (i <- 0 until 4) {
			multLocal(row, i, m)
		}
		
		this
	}
	
	protected def multColumnLocal(column: Int, m: Double) = {
		for (i <- 0 until 4) {
			multLocal(i, column, m)
		}
		
		this
	}
	
	protected def scaleLocal(x: Double = 1.0, y: Double = 1.0, z: Double = 1.0, w: Double = 1.0): Matrix4 = {
		multRowLocal(0, x)
		multRowLocal(1, y)
		multRowLocal(2, z)
		multRowLocal(3, w)
		
		this
	}
	
	protected def scaleLocal(v: Vector3): Matrix4 = scaleLocal(v.x, v.y, v.z)
	
	protected def invertLocal() = {
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
		    
		    setLocal(t00 * inv, t10 * inv, t20 * inv, t30 * inv,
		             t01 * inv, t11 * inv, t21 * inv, t31 * inv,
		             t02 * inv, t12 * inv, t22 * inv, t32 * inv,
		             t03 * inv, t13 * inv, t23 * inv, t33 * inv)
		}
		
		this
	}
	
	protected def rotateLocal(v: Vector3): Matrix4 = rotateLocal(v.x, v.y, v.z)
	
	protected def rotateLocal(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = {
		if ((x != 0.0) || (y != 0.0) || (z != 0.0)) {
			val cx = Math.cos(x)
			val sx = Math.sin(x)
			val cy = Math.cos(-y)
			val sy = Math.sin(-y)
			val cz = Math.cos(z)
			val sz = Math.sin(z)
			
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

			m00 = t00
			m01 = t01
			m02 = t02
	
			m10 = t10
			m11 = t11
			m12 = t12
	
			m20 = t20
			m21 = t21
			m22 = t22
	
			m30 = t30
			m31 = t31
			m32 = t32
		}
		
		this
	}
	
	protected def multLocal(m: Matrix4) = {
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
	
	    setLocal(t00, t01, t02, t03,
	    		 t10, t11, t12, t13,
	    		 t20, t21, t22, t23,
	    		 t30, t31, t32, t33)
	}
	
	protected def translateLocal(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = {
	    val t03 = m03 + m00 * x + m01 * y + m02 * z
	    val t13 = m13 + m10 * x + m11 * y + m12 * z
	    val t23 = m23 + m20 * x + m21 * y + m22 * z
	    val t33 = m33 + m30 * x + m31 * y + m32 * z
	
	    setLocal(m00, m01, m02, t03,
	    		 m10, m11, m12, t13,
	    		 m20, m21, m22, t23,
	    		 m30, m31, m32, t33)
	}
	
	protected def identityLocal() = setLocal(Matrix4.Identity)
	
/////// Extras ////////////
	
	def iterator = new Matrix4Iterator(this)
	
	override def toString() = {
	    val buffer = new StringBuffer()
	
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
	
	    buffer.append("Matrix4[\n")
	    appendRow(m00, m01, m02, m03)
	    appendRow(m10, m11, m12, m13)
	    appendRow(m20, m21, m22, m23)
	    appendRow(m30, m31, m32, m33)
	    buffer.append("]")
	
	    buffer.toString()
	}
	
	def copy() = Matrix4(this)
	
	/**
	 * Get a reference to the backing DoubleBuffer. Direct modification of
	 * this backing buffer will modify the Matrix4.
	 */
	def buffer = b
	
	/**
	 * Creates a copy of the backing DoubleBuffer and returns it.
	 */
	def bufferCopy = {
		val t = createBuffer()
		
		toBuffer(t, false)
		
		t
	}
}

class Matrix4Iterator protected(m: Matrix4) extends Iterator[Double] {
	var position = 0
	
	def next() = {
		val f = m.b.get(position)
		position += 1
		
		f
	}
	
	def hasNext = position < 16
}

object Matrix4 {
	val Zero = apply(m00 = 0.0, m11 = 0.0, m22 = 0.0, m33 = 0.0)
	val Identity = apply()
	
	def createBuffer() = ByteBuffer.allocateDirect(128).order(ByteOrder.nativeOrder).asDoubleBuffer
	
	def apply(m: Matrix4) = {
		val nm = new Matrix4()
		
		m.toBuffer(nm.b, false)
		
		nm
	}
	
	def apply(m00: Double = 1.0, m01: Double = 0.0, m02: Double = 0.0, m03: Double = 0.0,
			  m10: Double = 0.0, m11: Double = 1.0, m12: Double = 0.0, m13: Double = 0.0,
			  m20: Double = 0.0, m21: Double = 0.0, m22: Double = 1.0, m23: Double = 0.0,
			  m30: Double = 0.0, m31: Double = 0.0, m32: Double = 0.0, m33: Double = 1.0) = {
		val nm = new Matrix4()
		
		nm.setLocal(
				m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33
			)
		
		nm
	}
	
	def main(args: Array[String]): Unit = {
		Identity.setLocal(m01 = 34.0)
		println(Identity.toString)
	}
}