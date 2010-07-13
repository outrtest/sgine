package org.sgine.render

import java.nio.ByteBuffer
import java.nio.IntBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL14._
import org.lwjgl.opengl.GLContext

import org.sgine.property.AdvancedProperty

import scala.math._

/**
 * Represents a texture in the renderer.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Texture {
	def width: Int
	def height: Int
	
	/**
	 * Optional method to update the texture if an unapplied change is queued. This
	 * will automatically be done during bind if not done manually. This method is
	 * useful for updating state before drawing on-screen.
	 */
	def update(): Unit
	
	/**
	 * Bind the texture to be used. Must be called within the OpenGL thread.
	 */
	def bind(): Unit
	
	/**
	 * Unbinds the texture from the current OpenGL state.
	 */
	def unbind(): Unit
}

object Texture {
	@volatile var current: Int = -1
}