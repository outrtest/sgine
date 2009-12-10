package org.sgine.opengl.scene

import org.sgine.math._
import org.sgine.property._
import org.sgine.scene._

/**
 * GLNodeContainer simply adds a matrix location to determine offset for child nodes
 */
class GLNodeContainer extends GeneralNodeContainer with GLNode {
	override def invalidateMatrix() = {
		super.invalidateMatrix()
		
		for (n <- this) n match {
			case gl: GLNode => gl.invalidateMatrix()
		}
	}
	
	override def invalidateAlpha() = {
		super.invalidateAlpha()
		
		for (n <- this) n match {
			case gl: GLNode => gl.invalidateAlpha()
		}
	}
	
	override def +=(node: Node) {
		super.+=(node)
		
		invalidateMatrix()
		invalidateAlpha()
	}
}