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

package org.sgine.opengl.android

/**
 * Generated by org.sgine.opengl.generator.Generator
 * 
 * Documentation information pulled from <a href="http://www.opengl.org/sdk/docs/man/">http://www.opengl.org/sdk/docs/man/</a>.
 * 
 * @see org.sgine.opengl.generator.Generator
 */
class GL(instance: javax.microedition.khronos.opengles.GL11) extends org.sgine.opengl.GL {
	def glActiveTexture(texture: Int): Unit = {
		instance.glActiveTexture(texture)
	}

	def glAlphaFunc(func: Int, ref: Float): Unit = {
		instance.glAlphaFunc(func, ref)
	}

	def glBindBuffer(target: Int, buffer: Int): Unit = {
		instance.glBindBuffer(target, buffer)
	}

	def glBindTexture(target: Int, texture: Int): Unit = {
		instance.glBindTexture(target, texture)
	}

	def glBlendFunc(sfactor: Int, dfactor: Int): Unit = {
		instance.glBlendFunc(sfactor, dfactor)
	}

	def glBufferData(target: Int, size: Int, data: java.nio.Buffer, usage: Int): Unit = {
	  instance.glBufferData(target, size, data, usage)
	}

	def glBufferSubData(target: Int, offset: Int, size: Int, data: java.nio.Buffer): Unit = {
	  instance.glBufferSubData(target, offset, size, data)
	}

	def glClear(mask: Int): Unit = {
		instance.glClear(mask)
	}

	def glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
		instance.glClearColor(red, green, blue, alpha)
	}

	def glClearDepth(depth: Float): Unit = {
		instance.glClearDepthf(depth)
	}

	def glClearStencil(s: Int): Unit = {
		instance.glClearStencil(s)
	}

	def glClientActiveTexture(texture: Int): Unit = {
		instance.glClientActiveTexture(texture)
	}

  def glClipPlane(plane: Int, equation: java.nio.FloatBuffer): Unit = {
    instance.glClipPlanef(plane, equation)
  }

	def glColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
		instance.glColor4f(red, green, blue, alpha)
	}

	def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean): Unit = {
		instance.glColorMask(red, green, blue, alpha)
	}

  def glColorPointer(size: Int, `type`: Int, stride: Int, offset: Int): Unit = {
    instance.glColorPointer(size, `type`, stride, offset)
  }

  def glCompressedTexImage2D(target: Int, level: Int, internalFormat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: java.nio.Buffer): Unit = {
    instance.glCompressedTexImage2D(target, level, internalFormat, width, height, border, imageSize, data)
  }

	def glCopyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int): Unit = {
		instance.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border)
	}

	def glCopyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int): Unit = {
		instance.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height)
	}

	def glCullFace(mode: Int): Unit = {
		instance.glCullFace(mode)
	}

	def glDepthFunc(func: Int): Unit = {
		instance.glDepthFunc(func)
	}

	def glDepthMask(flag: Boolean): Unit = {
		instance.glDepthMask(flag)
	}

	def glDepthRange(zNear: Float, zFar: Float): Unit = {
		instance.glDepthRangef(zNear, zFar)
	}

	def glDisable(cap: Int): Unit = {
		instance.glDisable(cap)
	}

	def glDisableClientState(array: Int): Unit = {
		instance.glDisableClientState(array)
	}

	def glDrawArrays(mode: Int, first: Int, count: Int): Unit = {
		instance.glDrawArrays(mode, first, count)
	}

	def glEnable(cap: Int): Unit = {
		instance.glEnable(cap)
	}

	def glEnableClientState(array: Int): Unit = {
		instance.glEnableClientState(array)
	}

	def glFinish(): Unit = {
		instance.glFinish()
	}

	def glFlush(): Unit = {
		instance.glFlush()
	}

	def glFog(pname: Int, param: Float): Unit = {
		instance.glFogf(pname, param)
	}

	def glFrontFace(mode: Int): Unit = {
		instance.glFrontFace(mode)
	}

	def glGetError(): Int = {
		instance.glGetError()
	}

	def glGetString(name: Int): String = {
		instance.glGetString(name)
	}

	def glHint(target: Int, mode: Int): Unit = {
		instance.glHint(target, mode)
	}

	def glIsBuffer(buffer: Int): Boolean = {
		instance.glIsBuffer(buffer)
	}

	def glIsEnabled(cap: Int): Boolean = {
		instance.glIsEnabled(cap)
	}

	def glIsTexture(texture: Int): Boolean = {
		instance.glIsTexture(texture)
	}

	def glLight(light: Int, pname: Int, param: Float): Unit = {
		instance.glLightf(light, pname, param)
	}

	def glLightModel(pname: Int, param: Float): Unit = {
		instance.glLightModelf(pname, param)
	}

	def glLineWidth(width: Float): Unit = {
		instance.glLineWidth(width)
	}

	def glLoadIdentity(): Unit = {
		instance.glLoadIdentity()
	}

	def glLogicOp(opcode: Int): Unit = {
		instance.glLogicOp(opcode)
	}

	def glMaterial(face: Int, pname: Int, param: Float): Unit = {
		instance.glMaterialf(face, pname, param)
	}

	def glMatrixMode(mode: Int): Unit = {
		instance.glMatrixMode(mode)
	}

	def glMultiTexCoord(target: Int, s: Float, t: Float, r: Float, q: Float): Unit = {
		instance.glMultiTexCoord4f(target, s, t, r, q)
	}

	def glNormal(nx: Float, ny: Float, nz: Float): Unit = {
		instance.glNormal3f(nx, ny, nz)
	}

	def glPixelStore(pname: Int, param: Int): Unit = {
		instance.glPixelStorei(pname, param)
	}

	def glPointParameter(pname: Int, param: Float): Unit = {
		instance.glPointParameterf(pname, param)
	}

	def glPointSize(size: Float): Unit = {
		instance.glPointSize(size)
	}

	def glPolygonOffset(factor: Float, units: Float): Unit = {
		instance.glPolygonOffset(factor, units)
	}

	def glPopMatrix(): Unit = {
		instance.glPopMatrix()
	}

	def glPushMatrix(): Unit = {
		instance.glPushMatrix()
	}

	def glRotate(angle: Float, x: Float, y: Float, z: Float): Unit = {
		instance.glRotatef(angle, x, y, z)
	}

	def glSampleCoverage(value: Float, invert: Boolean): Unit = {
		instance.glSampleCoverage(value, invert)
	}

	def glScale(x: Float, y: Float, z: Float): Unit = {
		instance.glScalef(x, y, z)
	}

	def glScissor(x: Int, y: Int, width: Int, height: Int): Unit = {
		instance.glScissor(x, y, width, height)
	}

	def glShadeModel(mode: Int): Unit = {
		instance.glShadeModel(mode)
	}

	def glStencilFunc(func: Int, ref: Int, mask: Int): Unit = {
		instance.glStencilFunc(func, ref, mask)
	}

	def glStencilMask(mask: Int): Unit = {
		instance.glStencilMask(mask)
	}

	def glStencilOp(fail: Int, zfail: Int, zpass: Int): Unit = {
		instance.glStencilOp(fail, zfail, zpass)
	}

	def glTexEnv(target: Int, pname: Int, param: Float): Unit = {
		instance.glTexEnvf(target, pname, param)
	}

	def glTexEnv(target: Int, pname: Int, param: Int): Unit = {
		instance.glTexEnvi(target, pname, param)
	}

	def glTexParameter(target: Int, pname: Int, param: Float): Unit = {
		instance.glTexParameterf(target, pname, param)
	}

	def glTexParameter(target: Int, pname: Int, param: Int): Unit = {
		instance.glTexParameteri(target, pname, param)
	}

	def glTranslate(x: Float, y: Float, z: Float): Unit = {
		instance.glTranslatef(x, y, z)
	}

	def glViewport(x: Int, y: Int, width: Int, height: Int): Unit = {
		instance.glViewport(x, y, width, height)
	}
}