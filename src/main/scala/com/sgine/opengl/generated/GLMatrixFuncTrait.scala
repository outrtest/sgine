package com.sgine.opengl.generated

import com.sgine.opengl.GLContext
import javax.media.opengl._
import javax.media.opengl.fixedfunc._;

trait GLMatrixFuncTrait {
	val GL_MATRIX_MODE = GLMatrixFunc.GL_MATRIX_MODE;
	val GL_MODELVIEW = GLMatrixFunc.GL_MODELVIEW;
	val GL_PROJECTION = GLMatrixFunc.GL_PROJECTION;
	val GL_MODELVIEW_MATRIX = GLMatrixFunc.GL_MODELVIEW_MATRIX;
	val GL_PROJECTION_MATRIX = GLMatrixFunc.GL_PROJECTION_MATRIX;
	val GL_TEXTURE_MATRIX = GLMatrixFunc.GL_TEXTURE_MATRIX;


	def glGetFloatv(arg0:Int, arg1:Array[Float], arg2:Int):Unit = {
		GLContext.glmatrixfunc.glGetFloatv(arg0, arg1, arg2);
	}
	def glGetFloatv(arg0:Int, arg1:java.nio.FloatBuffer):Unit = {
		GLContext.glmatrixfunc.glGetFloatv(arg0, arg1);
	}
	def glGetIntegerv(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.glmatrixfunc.glGetIntegerv(arg0, arg1, arg2);
	}
	def glGetIntegerv(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.glmatrixfunc.glGetIntegerv(arg0, arg1);
	}
	def glMatrixMode(arg0:Int):Unit = {
		GLContext.glmatrixfunc.glMatrixMode(arg0);
	}
	def glPushMatrix():Unit = {
		GLContext.glmatrixfunc.glPushMatrix();
	}
	def glPopMatrix():Unit = {
		GLContext.glmatrixfunc.glPopMatrix();
	}
	def glLoadIdentity():Unit = {
		GLContext.glmatrixfunc.glLoadIdentity();
	}
	def glLoadMatrixf(arg0:java.nio.FloatBuffer):Unit = {
		GLContext.glmatrixfunc.glLoadMatrixf(arg0);
	}
	def glLoadMatrixf(arg0:Array[Float], arg1:Int):Unit = {
		GLContext.glmatrixfunc.glLoadMatrixf(arg0, arg1);
	}
	def glMultMatrixf(arg0:Array[Float], arg1:Int):Unit = {
		GLContext.glmatrixfunc.glMultMatrixf(arg0, arg1);
	}
	def glMultMatrixf(arg0:java.nio.FloatBuffer):Unit = {
		GLContext.glmatrixfunc.glMultMatrixf(arg0);
	}
	def glTranslatef(arg0:Float, arg1:Float, arg2:Float):Unit = {
		GLContext.glmatrixfunc.glTranslatef(arg0, arg1, arg2);
	}
	def glRotatef(arg0:Float, arg1:Float, arg2:Float, arg3:Float):Unit = {
		GLContext.glmatrixfunc.glRotatef(arg0, arg1, arg2, arg3);
	}
	def glScalef(arg0:Float, arg1:Float, arg2:Float):Unit = {
		GLContext.glmatrixfunc.glScalef(arg0, arg1, arg2);
	}
	def glOrthof(arg0:Float, arg1:Float, arg2:Float, arg3:Float, arg4:Float, arg5:Float):Unit = {
		GLContext.glmatrixfunc.glOrthof(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	def glFrustumf(arg0:Float, arg1:Float, arg2:Float, arg3:Float, arg4:Float, arg5:Float):Unit = {
		GLContext.glmatrixfunc.glFrustumf(arg0, arg1, arg2, arg3, arg4, arg5);
	}
}