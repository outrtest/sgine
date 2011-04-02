/*
 * Copyright (c) 2011 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.opengl.lwjgl

/**
 * Generated by org.sgine.opengl.generator.Generator
 * 
 * Documentation information pulled from <a href="http://www.opengl.org/sdk/docs/man/">http://www.opengl.org/sdk/docs/man/</a>.
 * 
 * @see org.sgine.opengl.generator.Generator
 */
class GL extends org.sgine.opengl.GL {
	def glActiveTexture(texture: Int): Unit = {
		org.lwjgl.opengl.GL13.glActiveTexture(texture)
	}

	def glAlphaFunc(func: Int, ref: Float): Unit = {
		org.lwjgl.opengl.GL11.glAlphaFunc(func, ref)
	}

	def glBindBuffer(target: Int, buffer: Int): Unit = {
		org.lwjgl.opengl.GL15.glBindBuffer(target, buffer)
	}

	def glBindTexture(target: Int, texture: Int): Unit = {
		org.lwjgl.opengl.GL11.glBindTexture(target, texture)
	}

	def glBlendFunc(sfactor: Int, dfactor: Int): Unit = {
		org.lwjgl.opengl.GL11.glBlendFunc(sfactor, dfactor)
	}

	def glBufferData(target: Int, size: Int, data: java.nio.Buffer, usage: Int): Unit = {
		data match {
		  case b: java.nio.IntBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
			case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
			case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
			case b: java.nio.ByteBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
			case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
			case _ => org.lwjgl.opengl.GL15.glBufferData(target, size, usage)
		}
	}

  def glBufferSubData(target: Int, offset: Int, size: Int, data: java.nio.Buffer): Unit = {
		data match {
			case b: java.nio.IntBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
			case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
			case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
			case b: java.nio.ByteBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
			case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
			case _ => throw new RuntimeException("Unknown buffer type: " + data)
		}
	}

	def glClear(mask: Int): Unit = {
		org.lwjgl.opengl.GL11.glClear(mask)
	}

	def glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
		org.lwjgl.opengl.GL11.glClearColor(red, green, blue, alpha)
	}

	def glClearDepth(depth: Float): Unit = {
		org.lwjgl.opengl.GL41.glClearDepthf(depth)
	}

	def glClearStencil(s: Int): Unit = {
		org.lwjgl.opengl.GL11.glClearStencil(s)
	}

	def glClientActiveTexture(texture: Int): Unit = {
		org.lwjgl.opengl.GL13.glClientActiveTexture(texture)
	}

  def glClipPlane(plane: Int, equation: java.nio.FloatBuffer): Unit = {
    org.lwjgl.opengl.GL11.glClipPlane(plane, org.sgine.opengl.GLUtil.toDoubleBuffer(equation))
  }

	def glColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
		org.lwjgl.opengl.GL11.glColor4f(red, green, blue, alpha)
	}

	def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean): Unit = {
		org.lwjgl.opengl.GL11.glColorMask(red, green, blue, alpha)
	}

  def glColorPointer(size: Int, `type`: Int, stride: Int, offset: Int): Unit = {
    org.lwjgl.opengl.GL11.glColorPointer(size, `type`, stride, offset)
  }

  def glCompressedTexImage2D(target: Int, level: Int, internalFormat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: java.nio.Buffer): Unit = {
    org.lwjgl.opengl.GL13.glCompressedTexImage2D(target, level, internalFormat, width, height, border, data.asInstanceOf[java.nio.ByteBuffer])
  }

	def glCopyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int): Unit = {
		org.lwjgl.opengl.GL11.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border)
	}

	def glCopyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int): Unit = {
		org.lwjgl.opengl.GL11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height)
	}

	def glCullFace(mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glCullFace(mode)
	}

	def glDepthFunc(func: Int): Unit = {
		org.lwjgl.opengl.GL11.glDepthFunc(func)
	}

	def glDepthMask(flag: Boolean): Unit = {
		org.lwjgl.opengl.GL11.glDepthMask(flag)
	}

	def glDepthRange(zNear: Float, zFar: Float): Unit = {
		org.lwjgl.opengl.GL41.glDepthRangef(zNear, zFar)
	}

	def glDisable(cap: Int): Unit = {
		org.lwjgl.opengl.GL11.glDisable(cap)
	}

	def glDisableClientState(array: Int): Unit = {
		org.lwjgl.opengl.GL11.glDisableClientState(array)
	}

	def glDrawArrays(mode: Int, first: Int, count: Int): Unit = {
		org.lwjgl.opengl.GL11.glDrawArrays(mode, first, count)
	}

	def glEnable(cap: Int): Unit = {
		org.lwjgl.opengl.GL11.glEnable(cap)
	}

	def glEnableClientState(array: Int): Unit = {
		org.lwjgl.opengl.GL11.glEnableClientState(array)
	}

	def glFinish(): Unit = {
		org.lwjgl.opengl.GL11.glFinish()
	}

	def glFlush(): Unit = {
		org.lwjgl.opengl.GL11.glFlush()
	}

	def glFog(pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glFogf(pname, param)
	}

	def glFrontFace(mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glFrontFace(mode)
	}

	def glGetError(): Int = {
		org.lwjgl.opengl.GL11.glGetError()
	}

	def glGetString(name: Int): String = {
		org.lwjgl.opengl.GL11.glGetString(name)
	}

	def glHint(target: Int, mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glHint(target, mode)
	}

	def glIsBuffer(buffer: Int): Boolean = {
		org.lwjgl.opengl.GL15.glIsBuffer(buffer)
	}

	def glIsEnabled(cap: Int): Boolean = {
		org.lwjgl.opengl.GL11.glIsEnabled(cap)
	}

	def glIsTexture(texture: Int): Boolean = {
		org.lwjgl.opengl.GL11.glIsTexture(texture)
	}

	def glLight(light: Int, pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glLightf(light, pname, param)
	}

	def glLightModel(pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glLightModelf(pname, param)
	}

	def glLineWidth(width: Float): Unit = {
		org.lwjgl.opengl.GL11.glLineWidth(width)
	}

	def glLoadIdentity(): Unit = {
		org.lwjgl.opengl.GL11.glLoadIdentity()
	}

	def glLogicOp(opcode: Int): Unit = {
		org.lwjgl.opengl.GL11.glLogicOp(opcode)
	}

	def glMaterial(face: Int, pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glMaterialf(face, pname, param)
	}

	def glMatrixMode(mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glMatrixMode(mode)
	}

	def glMultiTexCoord(target: Int, s: Float, t: Float, r: Float, q: Float): Unit = {
		org.lwjgl.opengl.GL13.glMultiTexCoord4f(target, s, t, r, q)
	}

	def glNormal(nx: Float, ny: Float, nz: Float): Unit = {
		org.lwjgl.opengl.GL11.glNormal3f(nx, ny, nz)
	}

	def glPixelStore(pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glPixelStorei(pname, param)
	}

	def glPointParameter(pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL14.glPointParameterf(pname, param)
	}

	def glPointSize(size: Float): Unit = {
		org.lwjgl.opengl.GL11.glPointSize(size)
	}

	def glPolygonOffset(factor: Float, units: Float): Unit = {
		org.lwjgl.opengl.GL11.glPolygonOffset(factor, units)
	}

	def glPopMatrix(): Unit = {
		org.lwjgl.opengl.GL11.glPopMatrix()
	}

	def glPushMatrix(): Unit = {
		org.lwjgl.opengl.GL11.glPushMatrix()
	}

	def glRotate(angle: Float, x: Float, y: Float, z: Float): Unit = {
		org.lwjgl.opengl.GL11.glRotatef(angle, x, y, z)
	}

	def glSampleCoverage(value: Float, invert: Boolean): Unit = {
		org.lwjgl.opengl.GL13.glSampleCoverage(value, invert)
	}

	def glScale(x: Float, y: Float, z: Float): Unit = {
		org.lwjgl.opengl.GL11.glScalef(x, y, z)
	}

	def glScissor(x: Int, y: Int, width: Int, height: Int): Unit = {
		org.lwjgl.opengl.GL11.glScissor(x, y, width, height)
	}

	def glShadeModel(mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glShadeModel(mode)
	}

	def glStencilFunc(func: Int, ref: Int, mask: Int): Unit = {
		org.lwjgl.opengl.GL11.glStencilFunc(func, ref, mask)
	}

	def glStencilMask(mask: Int): Unit = {
		org.lwjgl.opengl.GL11.glStencilMask(mask)
	}

	def glStencilOp(fail: Int, zfail: Int, zpass: Int): Unit = {
		org.lwjgl.opengl.GL11.glStencilOp(fail, zfail, zpass)
	}

	def glTexEnv(target: Int, pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glTexEnvf(target, pname, param)
	}

	def glTexEnv(target: Int, pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glTexEnvi(target, pname, param)
	}

	def glTexParameter(target: Int, pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glTexParameterf(target, pname, param)
	}

	def glTexParameter(target: Int, pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glTexParameteri(target, pname, param)
	}

	def glTranslate(x: Float, y: Float, z: Float): Unit = {
		org.lwjgl.opengl.GL11.glTranslatef(x, y, z)
	}

	def glViewport(x: Int, y: Int, width: Int, height: Int): Unit = {
		org.lwjgl.opengl.GL11.glViewport(x, y, width, height)
	}
}