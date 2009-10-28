package com.sgine.opengl.generated

import com.sgine.opengl.GLContext
import javax.media.opengl._
import javax.media.opengl.fixedfunc._;

trait GLES2Trait {
	val GL_NVIDIA_PLATFORM_BINARY_NV = GLES2.GL_NVIDIA_PLATFORM_BINARY_NV;


	def glGetShaderPrecisionFormat(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer, arg3:java.nio.IntBuffer):Unit = {
		GLContext.gles2.glGetShaderPrecisionFormat(arg0, arg1, arg2, arg3);
	}
	def glGetShaderPrecisionFormat(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int, arg4:Array[Int], arg5:Int):Unit = {
		GLContext.gles2.glGetShaderPrecisionFormat(arg0, arg1, arg2, arg3, arg4, arg5);
	}
}