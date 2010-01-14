package org.sgine.visual.shape

import org.sgine.math._
import org.sgine.scene._
import org.sgine.visual._

class Quad (p1: Vector3, p2: Vector3, p3: Vector3, p4: Vector3, material: Material) extends Shape with Node {
	def this(width: Double, height: Double, material: Material = null) = {
		this(Vector3.Zero, Vector3(width, 0.0, 0.0), Vector3(0.0, height, 0.0), Vector3(width, height, 0.0), material)
	}
	
	mesh := new QuadMesh(p1, p2, p3, p4)
}

case class QuadMesh(p1: Vector3, p2: Vector3, p3: Vector3, p4: Vector3) extends Mesh {
	val vertices = p1 :: p2 :: p3 :: p4 :: Nil
}