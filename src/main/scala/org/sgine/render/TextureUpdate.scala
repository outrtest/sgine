package org.sgine.render

import java.nio.ByteBuffer

import org.lwjgl.opengl.GL11._

case class TextureUpdate(x: Int, y: Int, width: Int, height: Int, buffer: ByteBuffer, textureFormat: Int = GL_RGBA, imageFormat: Int = GL_RGBA, imageType: Int = GL_UNSIGNED_BYTE)