package org.sgine.render.shape

import java.awt.geom.AffineTransform
import java.awt.geom.FlatteningPathIterator

import org.lwjgl.opengl.GL11._
import org.lwjgl.util.glu._
import org.lwjgl.util.glu.tessellation._

import scala.collection.mutable.ArrayBuffer

import scala.math._

import simplex3d.math.doublem._

object ShapeUtilities {
	def apply(awtShape: java.awt.Shape, shape: MutableShape) = {
		val tess = GLUtessellatorImpl.gluNewTess()
		
		val cb = new TessCallback()
		tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_BEGIN, cb)
		tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_VERTEX, cb)
		tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_COMBINE, cb)
		tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_END, cb)
		
		tess.gluBeginPolygon()
		
		val path = new FlatteningPathIterator(awtShape.getPathIterator(null), 0.001, 10)
		val coords = Array(0.0f, 0.0f)
		while(!path.isDone) {
			path.currentSegment(coords) match {
				case java.awt.geom.PathIterator.SEG_MOVETO => {
		        	tess.gluTessBeginContour()
		        	val a = Array(coords(0), coords(1), 0.0)
		        	tess.gluTessVertex(a, 0, a)
		        }
		        case java.awt.geom.PathIterator.SEG_LINETO => {
		        	val a = Array(coords(0), coords(1), 0.0)
		        	tess.gluTessVertex(a, 0, a)
		        }
		        case java.awt.geom.PathIterator.SEG_CLOSE => {
		        	tess.gluTessEndContour()
		        }
			}
			path.next()
		}
		
		tess.gluEndPolygon()
		
		shape.mode = ShapeMode.Triangles
		shape(VertexData(cb.buffer))
	}
	
	def convertTriangleFan(coords: Seq[Vec3d]) = {
		val data = new ArrayBuffer[Vec3d]
		val one = coords(0)
		var two = coords(1)
		
		for (i <- 2 until coords.length) {
			val three = coords(i)
			data += one
			data += two
			data += three
			
			two = three
		}
		
		data
	}
	
	def convertTriangleStrip(coords: Seq[Vec3d]) = {
		var data: List[Vec3d] = Nil
		var one: Vec3d = null
		var two: Vec3d = null
		
		for (v <- coords) {
			if ((one != null) && (two != null)) {
				data = v :: two :: one :: data
			}
			one = two
			two = v
		}
		
		data.reverse
	}
	
	def clip(shape: MutableShape, x1: Double, y1: Double, x2: Double, y2: Double) = shape.mode match {
		case ShapeMode.Quads => clipQuads(shape, x1, y1, x2, y2)
		case _ => throw new RuntimeException("Mode not supported for clipping: " + shape.mode)
	}
	
	def clipQuads(shape: MutableShape, x1: Double, y1: Double, x2: Double, y2: Double) = {
		var vertices: List[Vec3d] = Nil
		var texcoords: List[Vec2d] = Nil
		
		val vl = shape.vertexLatest
		val tl = shape.textureLatest
		
		for (index <- 0 until vl.length by 4) {
			val v1 = vl(index)
			val v2 = vl(index + 1)
			val v3 = vl(index + 2)
			val v4 = vl(index + 3)
			
			val o1 = outside(v1, x1, y1, x2, y2)
			val o2 = outside(v2, x1, y1, x2, y2)
			val o3 = outside(v3, x1, y1, x2, y2)
			val o4 = outside(v4, x1, y1, x2, y2)
			
			if (o1 && o2 && o3 && o4) {
				// Excluded
			} else if (o1 || o2 || o3 || o4) {
				// Needs to be adjusted
				val ow = abs(v2.x - v1.x)
				val oh = abs(v3.y - v1.y)
				
				var ux1 = v1.x
				var uy1 = v1.y
				var ux2 = v2.x
				var uy2 = v3.y
				
				if (ux1 < x1) {
					ux1 = x1
				}
				if (uy1 < y2) {
					uy1 = y2
				}
				if (ux2 > x2) {
					ux2 = x2
				}
				if (uy2 > y1) {
					uy2 = y1
				}
				
				val ax1 = abs(v1.x - ux1)
				val ay1 = abs(v1.y - uy1)
				val ax2 = abs(v2.x - ux2)
				val ay2 = abs(v3.y - uy2)
				
				val px1 = ax1 / ow
				val py1 = ay1 / oh
				val px2 = ax2 / ow
				val py2 = ay2 / oh
				
				println("Difference: " + ow + "x" + oh + " - (" + ax1 + "x" + ay1 + "x" + ax2 + "x" + ay2 + ")")
				
				vertices = Vec3d(ux1, uy1, v1.z) :: vertices
				vertices = Vec3d(ux2, uy1, v2.z) :: vertices
				vertices = Vec3d(ux2, uy2, v3.z) :: vertices
				vertices = Vec3d(ux1, uy2, v4.z) :: vertices
				
				if (tl != Nil) {
					texcoords = tl(index) :: texcoords
					texcoords = tl(index + 1) :: texcoords
					texcoords = tl(index + 2) :: texcoords
					texcoords = tl(index + 3) :: texcoords
				}
			} else {
				vertices = v1 :: vertices
				vertices = v2 :: vertices
				vertices = v3 :: vertices
				vertices = v4 :: vertices
				
				if (tl != Nil) {
					texcoords = tl(index) :: texcoords
					texcoords = tl(index + 1) :: texcoords
					texcoords = tl(index + 2) :: texcoords
					texcoords = tl(index + 3) :: texcoords
				}
			}
		}
		
		vertices = vertices.reverse
		texcoords = texcoords.reverse
		
		shape.vertex = VertexData(vertices)
		shape.texture = TextureData(texcoords)
	}
	
	def outside(v: Vec3d, x1: Double, y1: Double, x2: Double, y2: Double) = {
		if ((v.x < x1) || (v.x > x2)) {
			true
		} else if ((v.y < y2) || (v.y > y1)) {
			true
		} else {
			false
		}
	}
}

class TessCallback extends GLUtessellatorCallbackAdapter {
	val buffer = new ArrayBuffer[Vec3d]
	private var glType: Int = _
	private val shapeData = new ArrayBuffer[Vec3d]
	
	override def begin(t: Int) = {
		glType = t
	}
	
	override def vertex(vertexData: AnyRef) = {
		val data = vertexData.asInstanceOf[Array[Double]]
		shapeData += Vec3d(data(0), data(1), 0.0)
	}
	
	override def end() = {
		if (glType == GL_TRIANGLE_STRIP) {
			val d = ShapeUtilities.convertTriangleStrip(shapeData)
			buffer ++= d
		} else if (glType == GL_TRIANGLE_FAN) {
			val d = ShapeUtilities.convertTriangleFan(shapeData)
			buffer ++= d
		} else if (glType == GL_TRIANGLES) {
			buffer ++= shapeData
		} else {
			throw new RuntimeException("Unknown type: " + glType)
		}
		shapeData.clear()
	}
	
	override def combine(coords: Array[Double], data: Array[AnyRef], weight: Array[Float], outData: Array[AnyRef]) = {
		for (i <- 0 until outData.length) {
			val combined = new Array[Double](6)
			combined(0) = coords(0)
			combined(1) = coords(1)
			outData(i) = combined
		}
	}
}