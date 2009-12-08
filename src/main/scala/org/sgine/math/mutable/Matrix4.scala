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
	
	override def scale(x: D, y: D, z: D): Matrix4 = {
		m00 *= x
		m01 *= y
		m02 *= z
		
		m10 *= x
		m11 *= y
		m12 *= z
		
		m20 *= x
		m21 *= y
		m22 *= z
		
		m30 *= x
		m31 *= y
		m32 *= z
		
		this
	}

	override def scaleX(s: D): Matrix4 = {
		m00 *= s
		m10 *= s
		m20 *= s
		m30 *= s
		
		this
	}

	override def scaleY(s: D): Matrix4 = {
		m01 *= s
		m11 *= s
		m21 *= s
		m31 *= s
	
		this
	}

	override def scaleZ(s: D): Matrix4 = {
		m02 *= s
		m12 *= s
		m22 *= s
		m32 *= s
	
		this
	}
	
    override def transpose(): Matrix4 = {
    	val t01 = m10
    	m10 = m01
    	m01 = t01
    	
    	val t02 = m20
    	m20 = m02
    	m02 = t02
    	
    	val t03 = m30
    	m30 = m03
    	m03 = t03
    	
    	val t12 = m21
    	m21 = m12
    	m12 = t12
    	
    	val t13 = m31
    	m31 = m13
    	m13 = t13
    	
    	val t23 = m32
    	m32 = m23
    	m23 = t23
    	
    	this
    }
    
    override def invert: Matrix4 = {
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
    	
    	m00 = t00 * inv; m01 = t10 * inv; m02 = t20 * inv; m03 = t30 * inv
    	m10 = t01 * inv; m11 = t11 * inv; m12 = t21 * inv; m13 = t31 * inv
    	m20 = t02 * inv; m21 = t12 * inv; m22 = t22 * inv; m23 = t32 * inv
    	m30 = t03 * inv; m31 = t13 * inv; m32 = t23 * inv; m33 = t33 * inv
    	
    	this
    }
    
    override def rotX(a: D): Matrix4 = {
    	// TODO: Check that the unit is correct - should it be radians or Degrees
    	val d11 = Math.cos(a) // Was:  FastMath.cosDeg(a)
    	val d21 = Math.sin(a) // Was:  FastMath.sinDeg(a)
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

    	m01 = t01
    	m02 = t02
    	m11 = t11
    	m12 = t12
    	m21 = t21
    	m22 = t22
    	m31 = t31
    	m32 = t32
    	
    	this
    }
    
    override def rotY(a: D): Matrix4 = {
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
    	
    	m00 = t00
    	m02 = t02
    	m10 = t10
    	m12 = t12
    	m20 = t20
    	m22 = t22
    	m30 = t30
    	m32 = t32
    	
    	this
    }

    override def rotZ(a: D): Matrix4 = {
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
    	
    	m00 = t00
    	m01 = t01
    	m10 = t10
    	m11 = t11
    	m20 = t20
    	m21 = t21
    	m30 = t30
    	m31 = t31

    	this
    }
    
    override def rotate(x: D, y: D, z: D): Matrix4 = {
    	val negY = -y

    	val cx = Math.cos(x)
    	val sx = Math.sin(x)
    	val cy = Math.cos(negY)
    	val sy = Math.sin(negY)
    	val cz = Math.cos(z)
    	val sz = Math.sin(z)

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
    	
    	m00 = t00; m01 = t01; m02 = t02
    	m10 = t10; m11 = t11; m12 = t12
    	m20 = t20; m21 = t21; m22 = t22
    	m30 = t30; m31 = t31; m32 = t32
    	
    	this
    }
    
    override def mult(m: org.sgine.math.Matrix4): Matrix4 = {
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
    	
    	m00 = t00; m01 = t01; m02 = t02; m03 = t03
    	m10 = t10; m11 = t11; m12 = t12; m13 = t13
    	m20 = t20; m21 = t21; m22 = t22; m23 = t23
    	m30 = t30; m31 = t31; m32 = t32; m33 = t33
    	
    	this
    }
    
    def mult(m1: org.sgine.math.Matrix4, m2: org.sgine.math.Matrix4): Matrix4 = {
    	val t00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30
    	val t01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31
    	val t02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32
    	val t03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33

    	val t10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30
    	val t11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31
    	val t12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32
    	val t13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33

    	val t20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30
    	val t21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31
    	val t22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32
    	val t23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33

    	val t30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30
    	val t31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31
    	val t32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32
    	val t33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33
    	
    	m00 = t00; m01 = t01; m02 = t02; m03 = t03
    	m10 = t10; m11 = t11; m12 = t12; m13 = t13
    	m20 = t20; m21 = t21; m22 = t22; m23 = t23
    	m30 = t30; m31 = t31; m32 = t32; m33 = t33
    	
    	this
    }
    
    override def translate(x: D, y: D, z: D): Matrix4 = {
    	val t03 = m03 + m00 * x + m01 * y + m02 * z
    	val t13 = m13 + m10 * x + m11 * y + m12 * z
    	val t23 = m23 + m20 * x + m21 * y + m22 * z
    	val t33 = m33 + m30 * x + m31 * y + m32 * z

    	m03 = t03
    	m13 = t13
    	m23 = t23
    	m33 = t33
    	
    	this
    }

    override def translateX(x: D): Matrix4 = {
    	val t03 = m03 + m00 * x
    	val t13 = m13 + m10 * x
    	val t23 = m23 + m20 * x
    	val t33 = m33 + m30 * x

    	m03 = t03
    	m13 = t13
    	m23 = t23
    	m33 = t33
    	
    	this
    }

    override def translateY(y: D): Matrix4 = {
    	val t03 = m03 + m01 * y
    	val t13 = m13 + m11 * y
    	val t23 = m23 + m21 * y
    	val t33 = m33 + m31 * y

    	m03 = t03
    	m13 = t13
    	m23 = t23
    	m33 = t33
    	
    	this
    }

    override def translateZ(z: D): Matrix4 = {
    	val t03 = m03 + m02 * z
    	val t13 = m13 + m12 * z
    	val t23 = m23 + m22 * z
    	val t33 = m33 + m32 * z
    	
    	m03 = t03
    	m13 = t13
    	m23 = t23
    	m33 = t33
    	
    	this
    }
    
    def set(m: org.sgine.math.Matrix4): Matrix4 = {
    	m00 = m.m00; m01 = m.m01; m02 = m.m02; m03 = m.m03
    	m10 = m.m10; m11 = m.m11; m12 = m.m12; m13 = m.m13
    	m20 = m.m20; m21 = m.m21; m22 = m.m22; m23 = m.m23
    	m30 = m.m30; m31 = m.m31; m32 = m.m32; m33 = m.m33
    	
    	this
    }
    
    override def identity(): Matrix4 = set(org.sgine.math.Matrix4.Identity)
}

object Matrix4 {
	type D = Double
	
	def apply(): Matrix4 = new Matrix4()
	
	def apply(m: Matrix4): Matrix4 = {
		new Matrix4().set(m)
	}
	
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