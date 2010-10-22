package org.sgine.render.font

import org.sgine.core.HorizontalAlignment

import org.sgine.render.shape.MutableShape
import org.sgine.render.shape.ShapeMode
import org.sgine.render.shape.TextureData
import org.sgine.render.shape.VertexData

import scala.collection.mutable.ArrayBuffer

import simplex3d.math.doublem.renamed._

class TextBuilder {
	var text: String = _
	var kern = true
	var wrapWidth = -1.0
	var wrapMethod: TextWrapper = WordWrap
	var textAlignment: HorizontalAlignment = HorizontalAlignment.Center
	var xOffset = 0.0
	var yOffset = 0.0
	
	var vertices = new ArrayBuffer[Vec3]
	var texcoords = new ArrayBuffer[Vec2]
	var lines = new ArrayBuffer[RenderedLine]
	
	def apply(shape: MutableShape) = {
		shape.mode = ShapeMode.Quads
		shape.vertex = VertexData(vertices)
		shape.texture = TextureData(texcoords)
	}
	
	def reset() = {
		vertices.clear()
		texcoords.clear()
		lines.clear()
	}
}