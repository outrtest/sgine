package org.sgine.opengl

import org.lwjgl.opengl._;

object GLUtilities {
	private lazy val cap = GLContext.getCapabilities();
	
	def isVersion11 = cap.OpenGL11
	
	def isVersion12 = cap.OpenGL12
	
	def isVersion14 = cap.OpenGL14
	
	def isNPOT = cap.GL_ARB_texture_non_power_of_two
	
	def isMipmap = cap.GL_SGIS_generate_mipmap
}