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

import sys.error

/**
 * Generated by org.sgine.opengl.generator.OpenGLGenerator
 *
 * Documentation information pulled from <a href="http://www.opengl.org/sdk/docs/man/">http://www.opengl.org/sdk/docs/man/</a>.
 *
 * @see org.sgine.opengl.generator.OpenGLGenerator
 */
object GL extends org.sgine.opengl.GL {
	def glDeleteTexture(id: Int): Unit = {
		org.lwjgl.opengl.GL11.glDeleteTextures(id)
	}

	def glDeleteBuffer(id: Int): Unit = {
		org.lwjgl.opengl.GL15.glDeleteBuffers(id)
	}

	def glGenTexture(): Int = {
		org.lwjgl.opengl.GL11.glGenTextures()
	}

	def glGenBuffers(n: Int, buffers: Array[Int], offset: Int): Unit = {
		if (n > 0) {
		 buffers(offset) = org.lwjgl.opengl.GL15.glGenBuffers()
		 glGenBuffers(n - 1, buffers, offset + 1)
		}
	}

	def glGenBuffer(): Int = {
		org.lwjgl.opengl.GL15.glGenBuffers()
	}

	def glGetInteger(pname: Int): Int = {
		org.lwjgl.opengl.GL11.glGetInteger(pname)
	}

	def glGenBuffers(buffers: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL15.glGenBuffers(buffers)
	}

	def glDeleteBuffers(n: Int, buffers: Array[Int], offset: Int): Unit = {
		for (index <- offset until (offset + n)) {
		 org.lwjgl.opengl.GL15.glDeleteBuffers(buffers(index))
		}
	}

	def glBufferSubData(target: Int, offset: Long, data: java.nio.Buffer): Unit = {
		data match {
		 case b: java.nio.ByteBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
		 case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
		 case b: java.nio.IntBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
		 case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
		 case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL15.glBufferSubData(target, offset, b)
		 case _ => error("Failed conversion of buffer type: " + data.getClass.getName)
		}
	}

	def glVertexPointer(size: Int, `type`: Int, stride: Int, pointer: java.nio.Buffer): Unit = {
		pointer match {
		 case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL11.glVertexPointer(size, stride, b)
		 case b: java.nio.IntBuffer => org.lwjgl.opengl.GL11.glVertexPointer(size, stride, b)
		 case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL11.glVertexPointer(size, stride, b)
		 case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL11.glVertexPointer(size, stride, b)
		 case _ => error("Failed conversion of buffer type: " + pointer.getClass.getName)
		}
	}

	def glTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, `type`: Int, pixels: java.nio.Buffer): Unit = {
		pixels match {
		 case b: java.nio.ByteBuffer => org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, `type`, b)
		 case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, `type`, b)
		 case b: java.nio.IntBuffer => org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, `type`, b)
		 case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, `type`, b)
		 case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, `type`, b)
		 case _ => error("Failed conversion of buffer type: " + pixels.getClass.getName)
		}
	}

	def glTexImage2D(target: Int, level: Int, internalFormat: Int, width: Int, height: Int, border: Int, format: Int, `type`: Int, pixels: java.nio.Buffer): Unit = {
		pixels match {
		 case b: java.nio.ByteBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
		 case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
		 case b: java.nio.IntBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
		 case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
		 case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, b)
		 case null => org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, `type`, null.asInstanceOf[java.nio.FloatBuffer])
		 case _ => error("Failed conversion of buffer type: " + pixels.getClass.getName)
		}
	}

	def glTexCoordPointer(size: Int, `type`: Int, stride: Int, pointer: java.nio.Buffer): Unit = {
		pointer match {
		 case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL11.glTexCoordPointer(size, stride, b)
		 case b: java.nio.IntBuffer => org.lwjgl.opengl.GL11.glTexCoordPointer(size, stride, b)
		 case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL11.glTexCoordPointer(size, stride, b)
		 case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL11.glTexCoordPointer(size, stride, b)
		 case _ => error("Failed conversion of buffer type: " + pointer.getClass.getName)
		}
	}

	def glGenTextures(textures: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glGenTextures(textures)
	}

	def glDeleteTextures(n: Int, textures: Array[Int], offset: Int): Unit = {
		for (index <- offset until (offset + n)) {
		 org.lwjgl.opengl.GL11.glDeleteTextures(textures(index))
		}
	}

	def glColorPointer(size: Int, `type`: Int, stride: Int, pointer: java.nio.Buffer): Unit = {
		pointer match {
		 case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL11.glColorPointer(size, stride, b)
		 case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL11.glColorPointer(size, stride, b)
		 case _ => error("Failed conversion of buffer type: " + pointer.getClass.getName)
		}
	}

	def glBufferData(target: Int, size: Int, data: java.nio.Buffer, usage: Int): Unit = {
		data match {
		 case b: java.nio.ByteBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
		 case b: java.nio.ShortBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
		 case b: java.nio.IntBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
		 case b: java.nio.FloatBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
		 case b: java.nio.DoubleBuffer => org.lwjgl.opengl.GL15.glBufferData(target, b, usage)
		 case null => org.lwjgl.opengl.GL15.glBufferData(target, size, usage)
		 case _ => error("Failed conversion of buffer type: " + data.getClass.getName)
		}
	}

	def glViewport(x: Int, y: Int, width: Int, height: Int): Unit = {
		org.lwjgl.opengl.GL11.glViewport(x, y, width, height)
	}

	def glVertexPointer(size: Int, `type`: Int, stride: Int, offset: Int): Unit = {
		org.lwjgl.opengl.GL11.glVertexPointer(size, `type`, stride, offset)
	}

	def glVertexAttrib1f(indx: Int, x: Float): Unit = {
		org.lwjgl.opengl.GL20.glVertexAttrib1f(indx, x)
	}

	def glVertexAttrib2f(indx: Int, x: Float, y: Float): Unit = {
		org.lwjgl.opengl.GL20.glVertexAttrib2f(indx, x, y)
	}

	def glVertexAttrib3f(indx: Int, x: Float, y: Float, z: Float): Unit = {
		org.lwjgl.opengl.GL20.glVertexAttrib3f(indx, x, y, z)
	}

	def glVertexAttrib4f(indx: Int, x: Float, y: Float, z: Float, w: Float): Unit = {
		org.lwjgl.opengl.GL20.glVertexAttrib4f(indx, x, y, z, w)
	}

	def glValidateProgram(program: Int): Unit = {
		org.lwjgl.opengl.GL20.glValidateProgram(program)
	}

	def glUseProgram(program: Int): Unit = {
		org.lwjgl.opengl.GL20.glUseProgram(program)
	}

	def glUniform1f(location: Int, x: Float): Unit = {
		org.lwjgl.opengl.GL20.glUniform1f(location, x)
	}

	def glUniform1i(location: Int, x: Int): Unit = {
		org.lwjgl.opengl.GL20.glUniform1i(location, x)
	}

	def glUniform2f(location: Int, x: Float, y: Float): Unit = {
		org.lwjgl.opengl.GL20.glUniform2f(location, x, y)
	}

	def glUniform2i(location: Int, x: Int, y: Int): Unit = {
		org.lwjgl.opengl.GL20.glUniform2i(location, x, y)
	}

	def glUniform3f(location: Int, x: Float, y: Float, z: Float): Unit = {
		org.lwjgl.opengl.GL20.glUniform3f(location, x, y, z)
	}

	def glUniform3i(location: Int, x: Int, y: Int, z: Int): Unit = {
		org.lwjgl.opengl.GL20.glUniform3i(location, x, y, z)
	}

	def glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float): Unit = {
		org.lwjgl.opengl.GL20.glUniform4f(location, x, y, z, w)
	}

	def glUniform4i(location: Int, x: Int, y: Int, z: Int, w: Int): Unit = {
		org.lwjgl.opengl.GL20.glUniform4i(location, x, y, z, w)
	}

	def glTranslatef(x: Float, y: Float, z: Float): Unit = {
		org.lwjgl.opengl.GL11.glTranslatef(x, y, z)
	}

	def glTexParameter(target: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glTexParameter(target, pname, params)
	}

	def glTexParameterI(target: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL30.glTexParameterI(target, pname, params)
	}

	def glTexParameterIu(target: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL30.glTexParameterIu(target, pname, params)
	}

	def glTexParameterIi(target: Int, pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL30.glTexParameterIi(target, pname, param)
	}

	def glTexParameterf(target: Int, pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glTexParameterf(target, pname, param)
	}

	def glTexParameteri(target: Int, pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glTexParameteri(target, pname, param)
	}

	def glTexEnv(target: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glTexEnv(target, pname, params)
	}

	def glTexEnv(target: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glTexEnv(target, pname, params)
	}

	def glTexEnvf(target: Int, pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glTexEnvf(target, pname, param)
	}

	def glTexEnvi(target: Int, pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glTexEnvi(target, pname, param)
	}

	def glTexCoordPointer(size: Int, `type`: Int, stride: Int, offset: Int): Unit = {
		org.lwjgl.opengl.GL11.glTexCoordPointer(size, `type`, stride, offset)
	}

	def glStencilOpSeparate(face: Int, fail: Int, zfail: Int, zpass: Int): Unit = {
		org.lwjgl.opengl.GL20.glStencilOpSeparate(face, fail, zfail, zpass)
	}

	def glStencilOp(fail: Int, zfail: Int, zpass: Int): Unit = {
		org.lwjgl.opengl.GL11.glStencilOp(fail, zfail, zpass)
	}

	def glStencilMaskSeparate(face: Int, mask: Int): Unit = {
		org.lwjgl.opengl.GL20.glStencilMaskSeparate(face, mask)
	}

	def glStencilMask(mask: Int): Unit = {
		org.lwjgl.opengl.GL11.glStencilMask(mask)
	}

	def glStencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int): Unit = {
		org.lwjgl.opengl.GL20.glStencilFuncSeparate(face, func, ref, mask)
	}

	def glStencilFunc(func: Int, ref: Int, mask: Int): Unit = {
		org.lwjgl.opengl.GL11.glStencilFunc(func, ref, mask)
	}

	def glShaderSource(shader: Int, string: String): Unit = {
		org.lwjgl.opengl.GL20.glShaderSource(shader, string)
	}

	def glShadeModel(mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glShadeModel(mode)
	}

	def glScissor(x: Int, y: Int, width: Int, height: Int): Unit = {
		org.lwjgl.opengl.GL11.glScissor(x, y, width, height)
	}

	def glScalef(x: Float, y: Float, z: Float): Unit = {
		org.lwjgl.opengl.GL11.glScalef(x, y, z)
	}

	def glSampleCoverage(value: Float, invert: Boolean): Unit = {
		org.lwjgl.opengl.GL13.glSampleCoverage(value, invert)
	}

	def glRotatef(angle: Float, x: Float, y: Float, z: Float): Unit = {
		org.lwjgl.opengl.GL11.glRotatef(angle, x, y, z)
	}

	def glPushMatrix(): Unit = {
		org.lwjgl.opengl.GL11.glPushMatrix()
	}

	def glPopMatrix(): Unit = {
		org.lwjgl.opengl.GL11.glPopMatrix()
	}

	def glPolygonOffset(factor: Float, units: Float): Unit = {
		org.lwjgl.opengl.GL11.glPolygonOffset(factor, units)
	}

	def glPointSize(size: Float): Unit = {
		org.lwjgl.opengl.GL11.glPointSize(size)
	}

	def glPointParameter(pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL14.glPointParameter(pname, params)
	}

	def glPointParameteri(pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL14.glPointParameteri(pname, param)
	}

	def glPointParameter(pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL14.glPointParameter(pname, params)
	}

	def glPointParameterf(pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL14.glPointParameterf(pname, param)
	}

	def glPixelStorei(pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glPixelStorei(pname, param)
	}

	def glOrtho(left: Float, right: Float, bottom: Float, top: Float, zNear: Float, zFar: Float): Unit = {
		org.lwjgl.opengl.GL11.glOrtho(left, right, bottom, top, zNear, zFar)
	}

	def glNormalPointer(`type`: Int, stride: Int, offset: Int): Unit = {
		org.lwjgl.opengl.GL11.glNormalPointer(`type`, stride, offset)
	}

	def glNormal3i(nx: Int, ny: Int, nz: Int): Unit = {
		org.lwjgl.opengl.GL11.glNormal3i(nx, ny, nz)
	}

	def glNormal3f(nx: Float, ny: Float, nz: Float): Unit = {
		org.lwjgl.opengl.GL11.glNormal3f(nx, ny, nz)
	}

	def glMultiTexCoord4f(target: Int, s: Float, t: Float, r: Float, q: Float): Unit = {
		org.lwjgl.opengl.GL13.glMultiTexCoord4f(target, s, t, r, q)
	}

	def glMultMatrix(m: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glMultMatrix(m)
	}

	def glMatrixMode(mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glMatrixMode(mode)
	}

	def glMaterial(face: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glMaterial(face, pname, params)
	}

	def glMateriali(face: Int, pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glMateriali(face, pname, param)
	}

	def glMaterial(face: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glMaterial(face, pname, params)
	}

	def glMaterialf(face: Int, pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glMaterialf(face, pname, param)
	}

	def glLogicOp(opcode: Int): Unit = {
		org.lwjgl.opengl.GL11.glLogicOp(opcode)
	}

	def glLoadMatrix(m: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glLoadMatrix(m)
	}

	def glLoadIdentity(): Unit = {
		org.lwjgl.opengl.GL11.glLoadIdentity()
	}

	def glLinkProgram(program: Int): Unit = {
		org.lwjgl.opengl.GL20.glLinkProgram(program)
	}

	def glLineWidth(width: Float): Unit = {
		org.lwjgl.opengl.GL11.glLineWidth(width)
	}

	def glLightModel(pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glLightModel(pname, params)
	}

	def glLightModeli(pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glLightModeli(pname, param)
	}

	def glLightModel(pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glLightModel(pname, params)
	}

	def glLightModelf(pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glLightModelf(pname, param)
	}

	def glLight(light: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glLight(light, pname, params)
	}

	def glLighti(light: Int, pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glLighti(light, pname, param)
	}

	def glLight(light: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glLight(light, pname, params)
	}

	def glLightf(light: Int, pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glLightf(light, pname, param)
	}

	def glIsTexture(texture: Int): Boolean = {
		org.lwjgl.opengl.GL11.glIsTexture(texture)
	}

	def glIsShader(shader: Int): Boolean = {
		org.lwjgl.opengl.GL20.glIsShader(shader)
	}

	def glIsProgram(program: Int): Boolean = {
		org.lwjgl.opengl.GL20.glIsProgram(program)
	}

	def glIsEnabled(cap: Int): Boolean = {
		org.lwjgl.opengl.GL11.glIsEnabled(cap)
	}

	def glIsBuffer(buffer: Int): Boolean = {
		org.lwjgl.opengl.GL15.glIsBuffer(buffer)
	}

	def glHint(target: Int, mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glHint(target, mode)
	}

	def glGetVertexAttrib(index: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL20.glGetVertexAttrib(index, pname, params)
	}

	def glGetVertexAttribIu(index: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL30.glGetVertexAttribIu(index, pname, params)
	}

	def glGetUniformLocation(program: Int, name: String): Int = {
		org.lwjgl.opengl.GL20.glGetUniformLocation(program, name)
	}

	def glGetUniform(program: Int, location: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL20.glGetUniform(program, location, params)
	}

	def glGetUniformu(program: Int, location: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL30.glGetUniformu(program, location, params)
	}

	def glGetTexParameter(target: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glGetTexParameter(target, pname, params)
	}

	def glGetTexParameterI(target: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL30.glGetTexParameterI(target, pname, params)
	}

	def glGetTexParameterIu(target: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL30.glGetTexParameterIu(target, pname, params)
	}

	def glGetTexEnv(env: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glGetTexEnv(env, pname, params)
	}

	def glGetTexEnv(env: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glGetTexEnv(env, pname, params)
	}

	def glGetString(name: Int): String = {
		org.lwjgl.opengl.GL11.glGetString(name)
	}

	def glGetShader(shader: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL20.glGetShader(shader, pname, params)
	}

	def glGetProgram(program: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL20.glGetProgram(program, pname, params)
	}

	def glGetMaterial(face: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glGetMaterial(face, pname, params)
	}

	def glGetMaterial(face: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glGetMaterial(face, pname, params)
	}

	def glGetLight(light: Int, pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glGetLight(light, pname, params)
	}

	def glGetLight(light: Int, pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glGetLight(light, pname, params)
	}

	def glGetError(): Int = {
		org.lwjgl.opengl.GL11.glGetError()
	}

	def glGetAttribLocation(program: Int, name: String): Int = {
		org.lwjgl.opengl.GL20.glGetAttribLocation(program, name)
	}

	def glFrustum(left: Float, right: Float, bottom: Float, top: Float, zNear: Float, zFar: Float): Unit = {
		org.lwjgl.opengl.GL11.glFrustum(left, right, bottom, top, zNear, zFar)
	}

	def glFrontFace(mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glFrontFace(mode)
	}

	def glFog(pname: Int, params: java.nio.FloatBuffer): Unit = {
		org.lwjgl.opengl.GL11.glFog(pname, params)
	}

	def glFogi(pname: Int, param: Int): Unit = {
		org.lwjgl.opengl.GL11.glFogi(pname, param)
	}

	def glFog(pname: Int, params: java.nio.IntBuffer): Unit = {
		org.lwjgl.opengl.GL11.glFog(pname, params)
	}

	def glFogf(pname: Int, param: Float): Unit = {
		org.lwjgl.opengl.GL11.glFogf(pname, param)
	}

	def glFlush(): Unit = {
		org.lwjgl.opengl.GL11.glFlush()
	}

	def glFinish(): Unit = {
		org.lwjgl.opengl.GL11.glFinish()
	}

	def glEnableVertexAttribArray(index: Int): Unit = {
		org.lwjgl.opengl.GL20.glEnableVertexAttribArray(index)
	}

	def glEnableClientState(array: Int): Unit = {
		org.lwjgl.opengl.GL11.glEnableClientState(array)
	}

	def glEnable(cap: Int): Unit = {
		org.lwjgl.opengl.GL11.glEnable(cap)
	}

	def glDrawElements(mode: Int, count: Int, `type`: Int, offset: Int): Unit = {
		org.lwjgl.opengl.GL11.glDrawElements(mode, count, `type`, offset)
	}

	def glDrawArrays(mode: Int, first: Int, count: Int): Unit = {
		org.lwjgl.opengl.GL11.glDrawArrays(mode, first, count)
	}

	def glDisableVertexAttribArray(index: Int): Unit = {
		org.lwjgl.opengl.GL20.glDisableVertexAttribArray(index)
	}

	def glDisableClientState(array: Int): Unit = {
		org.lwjgl.opengl.GL11.glDisableClientState(array)
	}

	def glDisable(cap: Int): Unit = {
		org.lwjgl.opengl.GL11.glDisable(cap)
	}

	def glDetachShader(program: Int, shader: Int): Unit = {
		org.lwjgl.opengl.GL20.glDetachShader(program, shader)
	}

	def glDepthRange(zNear: Float, zFar: Float): Unit = {
		org.lwjgl.opengl.GL11.glDepthRange(zNear, zFar)
	}

	def glDepthRangef(zNear: Float, zFar: Float): Unit = {
		org.lwjgl.opengl.GL41.glDepthRangef(zNear, zFar)
	}

	def glDepthMask(flag: Boolean): Unit = {
		org.lwjgl.opengl.GL11.glDepthMask(flag)
	}

	def glDepthFunc(func: Int): Unit = {
		org.lwjgl.opengl.GL11.glDepthFunc(func)
	}

	def glDeleteShader(shader: Int): Unit = {
		org.lwjgl.opengl.GL20.glDeleteShader(shader)
	}

	def glDeleteProgram(program: Int): Unit = {
		org.lwjgl.opengl.GL20.glDeleteProgram(program)
	}

	def glCullFace(mode: Int): Unit = {
		org.lwjgl.opengl.GL11.glCullFace(mode)
	}

	def glCreateShader(`type`: Int): Int = {
		org.lwjgl.opengl.GL20.glCreateShader(`type`)
	}

	def glCreateProgram(): Int = {
		org.lwjgl.opengl.GL20.glCreateProgram()
	}

	def glCopyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int): Unit = {
		org.lwjgl.opengl.GL11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height)
	}

	def glCopyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int): Unit = {
		org.lwjgl.opengl.GL11.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border)
	}

	def glCompileShader(shader: Int): Unit = {
		org.lwjgl.opengl.GL20.glCompileShader(shader)
	}

	def glColorPointer(size: Int, `type`: Int, stride: Int, offset: Int): Unit = {
		org.lwjgl.opengl.GL11.glColorPointer(size, `type`, stride, offset)
	}

	def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean): Unit = {
		org.lwjgl.opengl.GL11.glColorMask(red, green, blue, alpha)
	}

	def glColor4f(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
		org.lwjgl.opengl.GL11.glColor4f(red, green, blue, alpha)
	}

	def glClientActiveTexture(texture: Int): Unit = {
		org.lwjgl.opengl.GL13.glClientActiveTexture(texture)
	}

	def glClearStencil(s: Int): Unit = {
		org.lwjgl.opengl.GL11.glClearStencil(s)
	}

	def glClearDepth(depth: Float): Unit = {
		org.lwjgl.opengl.GL11.glClearDepth(depth)
	}

	def glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
		org.lwjgl.opengl.GL11.glClearColor(red, green, blue, alpha)
	}

	def glClear(mask: Int): Unit = {
		org.lwjgl.opengl.GL11.glClear(mask)
	}

	def glBlendFuncSeparate(srcRGB: Int, dstRGB: Int, srcAlpha: Int, dstAlpha: Int): Unit = {
		org.lwjgl.opengl.GL14.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha)
	}

	def glBlendFunc(sfactor: Int, dfactor: Int): Unit = {
		org.lwjgl.opengl.GL11.glBlendFunc(sfactor, dfactor)
	}

	def glBlendEquationSeparate(modeRGB: Int, modeAlpha: Int): Unit = {
		org.lwjgl.opengl.GL20.glBlendEquationSeparate(modeRGB, modeAlpha)
	}

	def glBlendEquation(mode: Int): Unit = {
		org.lwjgl.opengl.GL14.glBlendEquation(mode)
	}

	def glBlendColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
		org.lwjgl.opengl.GL14.glBlendColor(red, green, blue, alpha)
	}

	def glBindTexture(target: Int, texture: Int): Unit = {
		org.lwjgl.opengl.GL11.glBindTexture(target, texture)
	}

	def glBindBuffer(target: Int, buffer: Int): Unit = {
		org.lwjgl.opengl.GL15.glBindBuffer(target, buffer)
	}

	def glBindAttribLocation(program: Int, index: Int, name: String): Unit = {
		org.lwjgl.opengl.GL20.glBindAttribLocation(program, index, name)
	}

	def glAttachShader(program: Int, shader: Int): Unit = {
		org.lwjgl.opengl.GL20.glAttachShader(program, shader)
	}

	def glAlphaFunc(func: Int, ref: Float): Unit = {
		org.lwjgl.opengl.GL11.glAlphaFunc(func, ref)
	}

	def glActiveTexture(texture: Int): Unit = {
		org.lwjgl.opengl.GL13.glActiveTexture(texture)
	}
}