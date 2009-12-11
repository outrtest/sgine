package org.sgine.opengl.scene

import org.sgine.scene._
import org.sgine.work._

import java.nio._

import org.lwjgl.opengl.GL11._;

trait GLSpatial extends Node with GLNode {
	private lazy val buffer = GLSpatial.createBuffer()
	private var lastRender = 0L
	
	override def apply(time: Double) = {
		super.apply(time)
		
		validateMatrix()					// Make sure world matrix is correct
		validateAlpha()						// Make sure world alpha is correct
		
		matrix.world.getTranspose(buffer)	// Update buffer
		buffer.rewind()
		glLoadMatrix(buffer)				// Load the matrix into the GL context
		
		glColor4d(matrix.alpha, matrix.alpha, matrix.alpha, matrix.alpha)
		
		render(time)
		lastRender = WorkManager.time
	}
	
	def render(time: Double): Unit
	
	def waitForRender() = {
		val previousRender = lastRender
		while (previousRender == lastRender) {
			Thread.sleep(1)
		}
	}
}

object GLSpatial {
	def createBuffer(): DoubleBuffer = {
		val bb = ByteBuffer.allocateDirect(16 * 8)
		bb.order(ByteOrder.nativeOrder())
		bb.asDoubleBuffer()
	}
}