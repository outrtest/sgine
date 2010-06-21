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
	
	def bind(): Unit
	
	def unbind(): Unit
}