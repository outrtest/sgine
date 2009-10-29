package com.sgine.opengl

import javax.media.opengl._;

object GLContext {
	private val localGL = new ThreadLocal[GL];
	private var version11:Int = -1;
	private var version12:Int = -1;
	private var version14:Int = -1;
	private var npotSupport:Int = -1;
	private var mipmapSupport:Int = -1;
	
	def gl:GL = {
		localGL.get();
	}
	
	def gl_=(gl:GL) {
		localGL.set(gl);
	}
	
	def glbase():GLBase = {
		gl;
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
	
	def isVersion11 = {
		if (version11 == -1) {
			version11 = getExtension("GL_VERSION_1_1");
		}
		version11 == 1;
	}
	
	def isVersion12 = {
		if (version12 == -1) {
			version12 = getExtension("GL_VERSION_1_2");
		}
		version12 == 1;
	}
	
	def isVersion14 = {
		if (version14 == -1) {
			version14 = getExtension("GL_VERSION_1_4");
		}
		version14 == 1;
	}
	
	def isNPOT = {
		if (npotSupport == -1) {
			npotSupport = getExtension("GL_ARB_texture_non_power_of_two");
		}
		npotSupport == 1;
	}
	
	def isMipmap = {
		if (mipmapSupport == -1) {
			if (isVersion14) {
				mipmapSupport = 1;
			} else {
				mipmapSupport = getExtension("GL_SGIS_generate_mipmap");
			}
		}
		mipmapSupport == 1;
	}
	
	private def getExtension(s:String) = {
		if (localGL.get().isExtensionAvailable(s)) {
			1;
		} else {
			0;
		}
	}
}