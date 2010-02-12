package org.sgine.opengl.renderable

import org.sgine.math.Vector3
import org.lwjgl.opengl.GL11._;

trait ShapeRenderable extends VertexRenderable {
	override abstract def renderBegin(time: Double) = glBegin(GL_TRIANGLES)
	
	override abstract def renderVertex(time: Double, index: Int, vertex: Vector3) = {
		super.renderVertex(time, index, vertex)
		
		glVertex3d(vertex.x, vertex.y, vertex.z)
	}
	
	override abstract def renderEnd(time: Double) = glEnd()
}