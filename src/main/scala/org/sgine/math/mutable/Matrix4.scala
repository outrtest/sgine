package org.sgine.math.mutable

import org.sgine.math.{Matrix4 => ImmutableMatrix4}
import org.sgine.math.{Vector3 => ImmutableVector3}

class Matrix4 protected() extends ImmutableMatrix4 {
	override def m00_=(v: Double) = b.put(0, v)
	override def m10_=(v: Double) = b.put(1, v)
	override def m20_=(v: Double) = b.put(2, v)
	override def m30_=(v: Double) = b.put(3, v)
	override def m01_=(v: Double) = b.put(4, v)
	override def m11_=(v: Double) = b.put(5, v)
	override def m21_=(v: Double) = b.put(6, v)
	override def m31_=(v: Double) = b.put(7, v)
	override def m02_=(v: Double) = b.put(8, v)
	override def m12_=(v: Double) = b.put(9, v)
	override def m22_=(v: Double) = b.put(10, v)
	override def m32_=(v: Double) = b.put(11, v)
	override def m03_=(v: Double) = b.put(12, v)
	override def m13_=(v: Double) = b.put(13, v)
	override def m23_=(v: Double) = b.put(14, v)
	override def m33_=(v: Double) = b.put(15, v)
	
	override def setEntry(row: Int, column: Int, v: Double) = setEntryLocal(row, column, v).asInstanceOf[Matrix4]
	
	override def setColumn(column: Int, v: Double) = setColumnLocal(column, v).asInstanceOf[Matrix4]
	
	override def setRow(row: Int, v: Double) = setRowLocal(row, v).asInstanceOf[Matrix4]
	
	override def set(
			m00: Double = Double.NaN, m01: Double = Double.NaN, m02: Double = Double.NaN, m03: Double = Double.NaN,
			m10: Double = Double.NaN, m11: Double = Double.NaN, m12: Double = Double.NaN, m13: Double = Double.NaN,
			m20: Double = Double.NaN, m21: Double = Double.NaN, m22: Double = Double.NaN, m23: Double = Double.NaN,
			m30: Double = Double.NaN, m31: Double = Double.NaN, m32: Double = Double.NaN, m33: Double = Double.NaN
		   ) = setLocal(
		  		   				m00, m01, m02, m03,
		  		   				m10, m11, m12, m13,
		  		   				m20, m21, m22, m23,
		  		   				m30, m31, m32, m33
		  		   			  ).asInstanceOf[Matrix4]
	
	override def set(m: ImmutableMatrix4) = setLocal(m).asInstanceOf[Matrix4]
	
	override def mult(row: Int, column: Int, m: Double) = multLocal(row, column, m).asInstanceOf[Matrix4]
	
	override def mult(m: ImmutableMatrix4) = multLocal(m).asInstanceOf[Matrix4]
	
	override def multRow(row: Int, m: Double) = multRowLocal(row, m).asInstanceOf[Matrix4]
	
	override def multColumn(column: Int, m: Double) = multColumnLocal(column, m).asInstanceOf[Matrix4]
	
	override def scale(x: Double = 1.0, y: Double = 1.0, z: Double = 1.0, w: Double = 1.0) = scaleLocal(x, y, z, w).asInstanceOf[Matrix4]
	
	override def scale(v: ImmutableVector3): Matrix4 = scale(v.x, v.y, v.z).asInstanceOf[Matrix4]
	
	override def invert() = invertLocal().asInstanceOf[Matrix4]
	
	override def rotate(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = rotateLocal(x, y, z).asInstanceOf[Matrix4]
	
	override def rotate(v: ImmutableVector3): Matrix4 = rotate(v.x, v.y, v.z).asInstanceOf[Matrix4]
	
	override def translate(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = translateLocal(x, y, z).asInstanceOf[Matrix4]
	
	def identity() = {
		identityLocal()
		
		this
	}
	
	override def copy() = Matrix4(this)
	
	def copyImmutable() = super.copy()
}

object Matrix4 {
	val Zero = apply(m00 = 0.0, m11 = 0.0, m22 = 0.0, m33 = 0.0)
	val Identity = apply()
	
	def apply(m: Matrix4) = new Matrix4().set(m)
	
	def apply(m00: Double = 1.0, m01: Double = 0.0, m02: Double = 0.0, m03: Double = 0.0,
			  m10: Double = 0.0, m11: Double = 1.0, m12: Double = 0.0, m13: Double = 0.0,
			  m20: Double = 0.0, m21: Double = 0.0, m22: Double = 1.0, m23: Double = 0.0,
			  m30: Double = 0.0, m31: Double = 0.0, m32: Double = 0.0, m33: Double = 1.0) = {
		val nm = new Matrix4()
		
		nm.set(
				m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33
			)
	}
}