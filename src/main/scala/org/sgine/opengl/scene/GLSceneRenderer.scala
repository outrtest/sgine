package org.sgine.opengl.scene

import org.sgine.scene._
import org.sgine.scene.view._

class GLSceneRenderer(container: NodeContainer) extends Function1[Double, Unit] {
	val view = NodeView(container, GLNodeQuery, false)
	
	def apply(time: Double) = {
		for (n <- view) n match {
			case gl: GLNode => gl(time)
		}
	}
}