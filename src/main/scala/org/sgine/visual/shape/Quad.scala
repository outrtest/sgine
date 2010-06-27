package org.sgine.visual.shape

import org.sgine.math._
import org.sgine.scene._
import org.sgine.visual._

class Quad (p1: Vector3, p2: Vector3, p3: Vector3, p4: Vector3, p5: Vector3, p6: Vector3, m: Material) extends Shape with Node {
	def this(width: Float, height: Float, material: Material = null) = {
		this(
				Vector3(width / -2.0f, height / -2.0f, 0.0f),			// Bottom-Left
				Vector3(width / 2.0f, height / -2.0f, 0.0f),			// Bottom-Right
				Vector3(width / 2.0f, height / 2.0f, 0.0f),			// Top-Right
				Vector3(width / -2.0f, height / 2.0f, 0.0f),			// Top-Left
				Vector3(width / -2.0f, height / -2.0f, 0.0f),			// Bottom-Left
				Vector3(width / 2.0f, height / 2.0f, 0.0f),			// Top-Right
				material
			)
	}
	
	mesh := new QuadMesh(p1, p2, p3, p4, p5, p6)
	material := m
}

case class QuadMesh(p1: Vector3, p2: Vector3, p3: Vector3, p4: Vector3, p5: Vector3, p6: Vector3) extends Mesh {
	val vertices = p1 :: p2 :: p3 :: p4 :: p5 :: p6 :: Nil
}