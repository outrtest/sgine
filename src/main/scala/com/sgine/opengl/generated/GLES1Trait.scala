package com.sgine.opengl.generated

import com.sgine.opengl.GLContext
import javax.media.opengl._
import javax.media.opengl.fixedfunc._;

trait GLES1Trait {
	val GL_POINT_SIZE_ARRAY_OES = GLES1.GL_POINT_SIZE_ARRAY_OES;
	val GL_POINT_SIZE_ARRAY_TYPE_OES = GLES1.GL_POINT_SIZE_ARRAY_TYPE_OES;
	val GL_POINT_SIZE_ARRAY_STRIDE_OES = GLES1.GL_POINT_SIZE_ARRAY_STRIDE_OES;
	val GL_POINT_SIZE_ARRAY_POINTER_OES = GLES1.GL_POINT_SIZE_ARRAY_POINTER_OES;
	val GL_POINT_SIZE_ARRAY_BUFFER_BINDING_OES = GLES1.GL_POINT_SIZE_ARRAY_BUFFER_BINDING_OES;
	val GL_ETC1_RGB8_OES = GLES1.GL_ETC1_RGB8_OES;
	val GL_TEXTURE_CROP_RECT_OES = GLES1.GL_TEXTURE_CROP_RECT_OES;
	val GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES = GLES1.GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES;
	val GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES = GLES1.GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES;
	val GL_TEXTURE_MATRIX_FLOAT_AS_INT_BITS_OES = GLES1.GL_TEXTURE_MATRIX_FLOAT_AS_INT_BITS_OES;
	val GL_MATRIX_INDEX_ARRAY_BUFFER_BINDING_OES = GLES1.GL_MATRIX_INDEX_ARRAY_BUFFER_BINDING_OES;
	val GL_HALF_FLOAT_OES = GLES1.GL_HALF_FLOAT_OES;


	def glAlphaFuncx(arg0:Int, arg1:Int):Unit = {
		GLContext.gles1.glAlphaFuncx(arg0, arg1);
	}
	def glClearColorx(arg0:Int, arg1:Int, arg2:Int, arg3:Int):Unit = {
		GLContext.gles1.glClearColorx(arg0, arg1, arg2, arg3);
	}
	def glClearDepthx(arg0:Int):Unit = {
		GLContext.gles1.glClearDepthx(arg0);
	}
	def glClipPlanef(arg0:Int, arg1:java.nio.FloatBuffer):Unit = {
		GLContext.gles1.glClipPlanef(arg0, arg1);
	}
	def glClipPlanef(arg0:Int, arg1:Array[Float], arg2:Int):Unit = {
		GLContext.gles1.glClipPlanef(arg0, arg1, arg2);
	}
	def glClipPlanex(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gles1.glClipPlanex(arg0, arg1, arg2);
	}
	def glClipPlanex(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glClipPlanex(arg0, arg1);
	}
	def glColor4x(arg0:Int, arg1:Int, arg2:Int, arg3:Int):Unit = {
		GLContext.gles1.glColor4x(arg0, arg1, arg2, arg3);
	}
	def glDepthRangex(arg0:Int, arg1:Int):Unit = {
		GLContext.gles1.glDepthRangex(arg0, arg1);
	}
	def glDrawTexfOES(arg0:Float, arg1:Float, arg2:Float, arg3:Float, arg4:Float):Unit = {
		GLContext.gles1.glDrawTexfOES(arg0, arg1, arg2, arg3, arg4);
	}
	def glDrawTexfvOES(arg0:Array[Float], arg1:Int):Unit = {
		GLContext.gles1.glDrawTexfvOES(arg0, arg1);
	}
	def glDrawTexfvOES(arg0:java.nio.FloatBuffer):Unit = {
		GLContext.gles1.glDrawTexfvOES(arg0);
	}
	def glDrawTexiOES(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int):Unit = {
		GLContext.gles1.glDrawTexiOES(arg0, arg1, arg2, arg3, arg4);
	}
	def glDrawTexivOES(arg0:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glDrawTexivOES(arg0);
	}
	def glDrawTexivOES(arg0:Array[Int], arg1:Int):Unit = {
		GLContext.gles1.glDrawTexivOES(arg0, arg1);
	}
	def glDrawTexsOES(arg0:Short, arg1:Short, arg2:Short, arg3:Short, arg4:Short):Unit = {
		GLContext.gles1.glDrawTexsOES(arg0, arg1, arg2, arg3, arg4);
	}
	def glDrawTexsvOES(arg0:Array[Short], arg1:Int):Unit = {
		GLContext.gles1.glDrawTexsvOES(arg0, arg1);
	}
	def glDrawTexsvOES(arg0:java.nio.ShortBuffer):Unit = {
		GLContext.gles1.glDrawTexsvOES(arg0);
	}
	def glDrawTexxOES(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int):Unit = {
		GLContext.gles1.glDrawTexxOES(arg0, arg1, arg2, arg3, arg4);
	}
	def glDrawTexxvOES(arg0:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glDrawTexxvOES(arg0);
	}
	def glDrawTexxvOES(arg0:Array[Int], arg1:Int):Unit = {
		GLContext.gles1.glDrawTexxvOES(arg0, arg1);
	}
	def glEGLImageTargetRenderbufferStorageOES(arg0:Int, arg1:java.nio.Buffer):Unit = {
		GLContext.gles1.glEGLImageTargetRenderbufferStorageOES(arg0, arg1);
	}
	def glEGLImageTargetTexture2DOES(arg0:Int, arg1:java.nio.Buffer):Unit = {
		GLContext.gles1.glEGLImageTargetTexture2DOES(arg0, arg1);
	}
	def glFogx(arg0:Int, arg1:Int):Unit = {
		GLContext.gles1.glFogx(arg0, arg1);
	}
	def glFogxv(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gles1.glFogxv(arg0, arg1, arg2);
	}
	def glFogxv(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glFogxv(arg0, arg1);
	}
	def glFrustumx(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int):Unit = {
		GLContext.gles1.glFrustumx(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	def glGetClipPlanef(arg0:Int, arg1:Array[Float], arg2:Int):Unit = {
		GLContext.gles1.glGetClipPlanef(arg0, arg1, arg2);
	}
	def glGetClipPlanef(arg0:Int, arg1:java.nio.FloatBuffer):Unit = {
		GLContext.gles1.glGetClipPlanef(arg0, arg1);
	}
	def glGetClipPlanex(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gles1.glGetClipPlanex(arg0, arg1, arg2);
	}
	def glGetClipPlanex(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glGetClipPlanex(arg0, arg1);
	}
	def glGetFixedv(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glGetFixedv(arg0, arg1);
	}
	def glGetFixedv(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gles1.glGetFixedv(arg0, arg1, arg2);
	}
	def glGetLightxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glGetLightxv(arg0, arg1, arg2, arg3);
	}
	def glGetLightxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glGetLightxv(arg0, arg1, arg2);
	}
	def glGetMaterialxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glGetMaterialxv(arg0, arg1, arg2, arg3);
	}
	def glGetMaterialxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glGetMaterialxv(arg0, arg1, arg2);
	}
	def glGetTexEnvxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glGetTexEnvxv(arg0, arg1, arg2, arg3);
	}
	def glGetTexEnvxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glGetTexEnvxv(arg0, arg1, arg2);
	}
	def glGetTexGenxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glGetTexGenxv(arg0, arg1, arg2, arg3);
	}
	def glGetTexGenxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glGetTexGenxv(arg0, arg1, arg2);
	}
	def glGetTexParameterxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glGetTexParameterxv(arg0, arg1, arg2);
	}
	def glGetTexParameterxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glGetTexParameterxv(arg0, arg1, arg2, arg3);
	}
	def glLightModelx(arg0:Int, arg1:Int):Unit = {
		GLContext.gles1.glLightModelx(arg0, arg1);
	}
	def glLightModelxv(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gles1.glLightModelxv(arg0, arg1, arg2);
	}
	def glLightModelxv(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glLightModelxv(arg0, arg1);
	}
	def glLightx(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gles1.glLightx(arg0, arg1, arg2);
	}
	def glLightxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glLightxv(arg0, arg1, arg2);
	}
	def glLightxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glLightxv(arg0, arg1, arg2, arg3);
	}
	def glLineWidthx(arg0:Int):Unit = {
		GLContext.gles1.glLineWidthx(arg0);
	}
	def glLoadMatrixx(arg0:Array[Int], arg1:Int):Unit = {
		GLContext.gles1.glLoadMatrixx(arg0, arg1);
	}
	def glLoadMatrixx(arg0:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glLoadMatrixx(arg0);
	}
	def glLoadPaletteFromModelViewMatrixOES():Unit = {
		GLContext.gles1.glLoadPaletteFromModelViewMatrixOES();
	}
	def glMaterialx(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gles1.glMaterialx(arg0, arg1, arg2);
	}
	def glMaterialxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glMaterialxv(arg0, arg1, arg2, arg3);
	}
	def glMaterialxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glMaterialxv(arg0, arg1, arg2);
	}
	def glMultMatrixx(arg0:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glMultMatrixx(arg0);
	}
	def glMultMatrixx(arg0:Array[Int], arg1:Int):Unit = {
		GLContext.gles1.glMultMatrixx(arg0, arg1);
	}
	def glMultiTexCoord4x(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int):Unit = {
		GLContext.gles1.glMultiTexCoord4x(arg0, arg1, arg2, arg3, arg4);
	}
	def glNormal3x(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gles1.glNormal3x(arg0, arg1, arg2);
	}
	def glOrthox(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int):Unit = {
		GLContext.gles1.glOrthox(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	def glPointParameterx(arg0:Int, arg1:Int):Unit = {
		GLContext.gles1.glPointParameterx(arg0, arg1);
	}
	def glPointParameterxv(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glPointParameterxv(arg0, arg1);
	}
	def glPointParameterxv(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gles1.glPointParameterxv(arg0, arg1, arg2);
	}
	def glPointSizePointerOES(arg0:Int, arg1:Int, arg2:java.nio.Buffer):Unit = {
		GLContext.gles1.glPointSizePointerOES(arg0, arg1, arg2);
	}
	def glPointSizex(arg0:Int):Unit = {
		GLContext.gles1.glPointSizex(arg0);
	}
	def glPolygonOffsetx(arg0:Int, arg1:Int):Unit = {
		GLContext.gles1.glPolygonOffsetx(arg0, arg1);
	}
	def glQueryMatrixxOES(arg0:java.nio.IntBuffer, arg1:java.nio.IntBuffer):Int = {
		GLContext.gles1.glQueryMatrixxOES(arg0, arg1);
	}
	def glQueryMatrixxOES(arg0:Array[Int], arg1:Int, arg2:Array[Int], arg3:Int):Int = {
		GLContext.gles1.glQueryMatrixxOES(arg0, arg1, arg2, arg3);
	}
	def glRotatex(arg0:Int, arg1:Int, arg2:Int, arg3:Int):Unit = {
		GLContext.gles1.glRotatex(arg0, arg1, arg2, arg3);
	}
	def glSampleCoveragex(arg0:Int, arg1:Boolean):Unit = {
		GLContext.gles1.glSampleCoveragex(arg0, arg1);
	}
	def glScalex(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gles1.glScalex(arg0, arg1, arg2);
	}
	def glTexEnvx(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gles1.glTexEnvx(arg0, arg1, arg2);
	}
	def glTexEnvxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glTexEnvxv(arg0, arg1, arg2);
	}
	def glTexEnvxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glTexEnvxv(arg0, arg1, arg2, arg3);
	}
	def glTexGenx(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gles1.glTexGenx(arg0, arg1, arg2);
	}
	def glTexGenxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glTexGenxv(arg0, arg1, arg2, arg3);
	}
	def glTexGenxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glTexGenxv(arg0, arg1, arg2);
	}
	def glTexParameterx(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gles1.glTexParameterx(arg0, arg1, arg2);
	}
	def glTexParameterxv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gles1.glTexParameterxv(arg0, arg1, arg2, arg3);
	}
	def glTexParameterxv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gles1.glTexParameterxv(arg0, arg1, arg2);
	}
	def glTranslatex(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gles1.glTranslatex(arg0, arg1, arg2);
	}
}