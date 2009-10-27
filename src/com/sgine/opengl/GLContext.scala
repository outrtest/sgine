package com.sgine.opengl

import javax.media.opengl._;

object GLContext {
	private val localGL = new ThreadLocal[GL];
	
	def gl:GL = {
		localGL.get();
	}
	
	def gl_=(gl:GL) {
		localGL.set(gl);
	}
	
	def gl2():GL2 = {
		gl.getGL2();
	}
	
	def gl3():GL3 = {
		gl.getGL3();
	}
	
	def gles1():GLES1 = {
		gl.getGLES1();
	}
	
	def gles2():GLES2 = {
		gl.getGLES2();
	}
	
	def gl2es1():GL2ES1 = {
		gl.getGL2ES1();
	}
	
	def gl2es2():GL2ES2 = {
		gl.getGL2ES2();
	}

	def glmatrixfunc():GL2 = {
		gl2();
	}
}