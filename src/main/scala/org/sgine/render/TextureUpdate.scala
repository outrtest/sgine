package org.sgine.render

import java.nio.ByteBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._

case class TextureUpdate(x: Int, y: Int, width: Int, height: Int, buffer: ByteBuffer, textureFormat: Int, imageFormat: Int, imageType: Int)