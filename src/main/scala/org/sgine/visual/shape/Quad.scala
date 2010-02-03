package org.sgine.visual.shape

import org.sgine.math._
import org.sgine.scene._
import org.sgine.visual._

class Quad (p1: Vector3, p2: Vector3, p3: Vector3, p4: Vector3, p5: Vector3, p6: Vector3, material: Material) extends Shape with Node {
	def this(width: Double, height: Double, material: Material = null) = {
		this(
				Vector3(width / -2.0, height / -2.0, 0.0),			// Bottom-Left
				Vector3(width / 2.0, height / -2.0, 0.0),			// Bottom-Right
				Vector3(width / 2.0, height / 2.0, 0.0),			// Top-Right
				Vector3(width / -2.0, height / 2.0, 0.0),			// Top-Left
				Vector3(width / -2.0, height / -2.0, 0.0),			// Bottom-Left
				Vector3(width / 2.0, height / 2.0, 0.0),			// Top-Right
				material
			)
	}
	
	mesh := new QuadMesh(p1, p2, p3, p4, p5, p6)
}

case class QuadMesh(p1: Vector3, p2: Vector3, p3: Vector3, p4: Vector3, p5: Vector3, p6: Vector3) extends Mesh {
	val vertices = p1 :: p2 :: p3 :: p4 :: p5 :: p6 :: Nil
}