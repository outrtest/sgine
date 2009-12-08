package org.sgine.opengl.scene

import org.sgine.scene._

import java.nio._

import org.lwjgl.opengl.GL11._;

trait GLSpatial extends Node with GLNode with Function1[Double, Unit] {
	private lazy val buffer = GLSpatial.createBuffer()
	
	def apply(time: Double) = {
		validateMatrix()					// Make sure world matrix is correct
		
		matrix.world.getTranspose(buffer)	// Update buffer
		glLoadMatrix(buffer)				// Load the matrix into the GL context
		
		render(time)
	}
	
	def render(time: Double): Unit
}

object GLSpatial {
	def createBuffer() = {
		val bb = ByteBuffer.allocateDirect(16 * 8)
		bb.order(ByteOrder.nativeOrder())
		bb.asDoubleBuffer()
	}
}