package org.sgine.opengl.scene

import org.sgine.math._

trait GLNode {
	val matrix = new Transform()
}

class Transform {
	val local = Matrix4()
	val world = Matrix4()
}