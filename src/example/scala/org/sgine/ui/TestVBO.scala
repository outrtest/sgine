package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Graphics3D._
import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.WorldMatrixNode
import org.sgine.scene.ext.ResolutionNode

import org.sgine.ui.ext._

import org.lwjgl._
import org.lwjgl.opengl._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._

import scala.math._

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

object TestVBO {
	private lazy val triangleBuffer = createTriangleBuffer()
	
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test VBO") //, 4, 8, 4, 4)
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with WorldMatrixNode
		val m = scene.worldMatrix()
		m := m scale(100.0) translate(Vec3(0.0, 0.0, -400.0))
		
		scene += new Component() {
			def drawComponent() = {
				glEnable(GL_VERTEX_ARRAY)
				glEnable(GL_COLOR_ARRAY)
				glBindBuffer(GL_ARRAY_BUFFER, triangleBuffer)
				glVertexPointer(3, GL_FLOAT, 0, 0)
				glColorPointer(3, GL_UNSIGNED_BYTE, 0, 9 * 12)
				glDrawArrays(GL_TRIANGLES, 0, 3)
			}
		}
		
		r.renderable := RenderableScene(scene)
	}
	
	private def createTriangleBuffer() = {
		val id = glGenBuffers()
		
		glBindBuffer(GL_ARRAY_BUFFER, id)
		
		val vertices = BufferUtils.createFloatBuffer(9)
		vertices.put(Array(1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f)).flip()
		
		val colors = BufferUtils.createByteBuffer(9)
		colors.put(Array(255.toByte, 50.toByte, 160.toByte, 180.toByte, 255.toByte, 70.toByte, 90.toByte, 10.toByte, 255.toByte)).flip()
		
		glBufferData(GL_ARRAY_BUFFER, 9 * (4 * 3 + 4), GL_STATIC_DRAW)
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices)
		glBufferSubData(GL_ARRAY_BUFFER, 9 * 12, colors)
		
		id
	}
}