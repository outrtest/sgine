package org.sgine.math.mutable

class Matrix4 extends org.sgine.math.Matrix4 {
	type D = Double
	
	override def m00_=(_m00: D) = this._m00 = _m00
	override def m01_=(_m01: D) = this._m01 = _m01
	override def m02_=(_m02: D) = this._m02 = _m02
	override def m03_=(_m03: D) = this._m03 = _m03
	
	override def m10_=(_m10: D) = this._m10 = _m10
	override def m11_=(_m11: D) = this._m11 = _m11
	override def m12_=(_m12: D) = this._m12 = _m12
	override def m13_=(_m13: D) = this._m13 = _m13
	
	override def m20_=(_m20: D) = this._m20 = _m20
	override def m21_=(_m21: D) = this._m21 = _m21
	override def m22_=(_m22: D) = this._m22 = _m22
	override def m23_=(_m23: D) = this._m23 = _m23
	
	override def m30_=(_m30: D) = this._m30 = _m30
	override def m31_=(_m31: D) = this._m31 = _m31
	override def m32_=(_m32: D) = this._m32 = _m32
	override def m33_=(_m33: D) = this._m33 = _m33
	
	override def scale(s: D): Matrix4 = {
		m00 *= s
		m01 *= s
		m02 *= s
		
        m10 *= s
        m11 *= s
        m12 *= s
        
        m20 *= s
        m21 *= s
        m22 *= s
        
        m30 *= s
        m31 *= s
        m32 *= s
		
		this
	}
}

object Matrix4 {
	type D = Double
	
	def apply(): Matrix4 = new Matrix4()
	
	def apply(
			_m00: D, _m01: D, _m02: D, _m03: D,
			_m10: D, _m11: D, _m12: D, _m13: D,
			_m20: D, _m21: D, _m22: D, _m23: D,
			_m30: D, _m31: D, _m32: D, _m33: D): Matrix4 = {
		
		val m = new Matrix4() 
		m._m00 = _m00
		m._m01 = _m01
		m._m02 = _m02
		m._m03 = _m03
		
		m._m10 = _m10
		m._m11 = _m11
		m._m12 = _m12
		m._m13 = _m13
		
		m._m20 = _m20
		m._m21 = _m21
		m._m22 = _m22
		m._m23 = _m23
		
		m._m30 = _m30
		m._m31 = _m31
		m._m32 = _m32
		m._m33 = _m33
		
		m
	}
}