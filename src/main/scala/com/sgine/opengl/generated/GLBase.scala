package com.sgine.opengl.generated

import com.sgine.opengl.GLContext
import javax.media.opengl._
import javax.media.opengl.fixedfunc._;

trait GLBase {


	def getContext():javax.media.opengl.GLContext = {
		GLContext.glbase.getContext();
	}
	def isGL():Boolean = {
		GLContext.glbase.isGL();
	}
	def isGL3():Boolean = {
		GLContext.glbase.isGL3();
	}
	def isGL2():Boolean = {
		GLContext.glbase.isGL2();
	}
	def isGLES1():Boolean = {
		GLContext.glbase.isGLES1();
	}
	def isGLES2():Boolean = {
		GLContext.glbase.isGLES2();
	}
	def isGLES():Boolean = {
		GLContext.glbase.isGLES();
	}
	def isGL2ES1():Boolean = {
		GLContext.glbase.isGL2ES1();
	}
	def isGL2ES2():Boolean = {
		GLContext.glbase.isGL2ES2();
	}
	def isGL2GL3():Boolean = {
		GLContext.glbase.isGL2GL3();
	}
	def hasGLSL():Boolean = {
		GLContext.glbase.hasGLSL();
	}
	def getGL():javax.media.opengl.GL = {
		GLContext.glbase.getGL();
	}
	def getGL3():javax.media.opengl.GL3 = {
		GLContext.glbase.getGL3();
	}
	def getGL2():javax.media.opengl.GL2 = {
		GLContext.glbase.getGL2();
	}
	def getGLES1():javax.media.opengl.GLES1 = {
		GLContext.glbase.getGLES1();
	}
	def getGLES2():javax.media.opengl.GLES2 = {
		GLContext.glbase.getGLES2();
	}
	def getGL2ES1():javax.media.opengl.GL2ES1 = {
		GLContext.glbase.getGL2ES1();
	}
	def getGL2ES2():javax.media.opengl.GL2ES2 = {
		GLContext.glbase.getGL2ES2();
	}
	def getGL2GL3():javax.media.opengl.GL2GL3 = {
		GLContext.glbase.getGL2GL3();
	}
	def getGLProfile():javax.media.opengl.GLProfile = {
		GLContext.glbase.getGLProfile();
	}
	def isFunctionAvailable(arg0:java.lang.String):Boolean = {
		GLContext.glbase.isFunctionAvailable(arg0);
	}
	def isExtensionAvailable(arg0:java.lang.String):Boolean = {
		GLContext.glbase.isExtensionAvailable(arg0);
	}
	def setSwapInterval(arg0:Int):Unit = {
		GLContext.glbase.setSwapInterval(arg0);
	}
	def getSwapInterval():Int = {
		GLContext.glbase.getSwapInterval();
	}
	def getPlatformGLExtensions():java.lang.Object = {
		GLContext.glbase.getPlatformGLExtensions();
	}
	def getExtension(arg0:java.lang.String):java.lang.Object = {
		GLContext.glbase.getExtension(arg0);
	}
}