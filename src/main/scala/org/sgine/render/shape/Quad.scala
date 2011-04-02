package org.sgine.render.shape

import org.sgine.core.Face
import org.sgine.core.Material

import org.sgine.render.Texture

import simplex3d.math.doublem.renamed._

class Quad protected(val width: Double,
				   val height: Double,
				   vertex: VertexData,
				   color: ColorData,
				   texture: TextureData,
				   normal: NormalData,
				   cull: Face,
				   material: Material) extends ImmutableShape(vertex, color, texture, normal, cull, material, ShapeMode.Quads)

object Quad {
	def apply(width: Double,
			  height: Double,
			  color: ColorData = null,
			  texture: TextureData = null,
			  normal: NormalData = null,
			  cull: Face = Face.Back,
			  material: Material = null) = {
		val w = width / 2.0
		val h = height / 2.0
		val vertex = VertexData(
						List(
							Vec3(-w, -h, 0.0),
							Vec3(w, -h, 0.0),
							Vec3(w, h, 0.0),
							Vec3(-w, h, 0.0)
						)
					 )
		new Quad(width, height, vertex, color, texture, normal, cull, material)
	}
	
	def subtexture(texture: Texture, x: Double, y: Double, width: Double, height: Double, scale: Double = 1.0, cull: Face = Face.Back) = {
		val quadWidth = width * scale
		val quadHeight = height * scale
		val x1 = x / texture.width
		val y1 = y / texture.height
		val x2 = (x + width) / texture.width
		val y2 = (y + height) / texture.height
		val td = TextureData(
					List(
						Vec2(x1, y2),
						Vec2(x2, y2),
						Vec2(x2, y1),
						Vec2(x1, y1)
					)
				 )
		apply(quadWidth, quadHeight, null, td, null, cull, null)
	}
}