package org.sgine.visual.shape

import org.sgine.scene._
import org.sgine.visual._

import simplex3d.math._
import simplex3d.math.doublem._

class Quad (p1: Vec3d, p2: Vec3d, p3: Vec3d, p4: Vec3d, p5: Vec3d, p6: Vec3d, m: Material) extends Shape with Node {
	def this(width: Float, height: Float, material: Material = null) = {
		this(
				Vec3d(width / -2.0f, height / -2.0f, 0.0f),			// Bottom-Left
				Vec3d(width / 2.0f, height / -2.0f, 0.0f),			// Bottom-Right
				Vec3d(width / 2.0f, height / 2.0f, 0.0f),			// Top-Right
				Vec3d(width / -2.0f, height / 2.0f, 0.0f),			// Top-Left
				Vec3d(width / -2.0f, height / -2.0f, 0.0f),			// Bottom-Left
				Vec3d(width / 2.0f, height / 2.0f, 0.0f),			// Top-Right
				material
			)
	}
	
	mesh := new QuadMesh(p1, p2, p3, p4, p5, p6)
	material := m
}

case class QuadMesh(p1: Vec3d, p2: Vec3d, p3: Vec3d, p4: Vec3d, p5: Vec3d, p6: Vec3d) extends Mesh {
	val vertices = p1 :: p2 :: p3 :: p4 :: p5 :: p6 :: Nil
}