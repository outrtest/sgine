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
				val ow = v2.x - v1.x
				val oh = v3.y - v1.y
				
				var uvx1 = v1.x
				var uvy1 = v1.y
				var uvx2 = v2.x
				var uvy2 = v3.y
				
				var utx1 = tl(index).x
				var uty1 = tl(index).y
				var utx2 = tl(index + 1).x
				var uty2 = tl(index + 2).y
				
				val cw = utx2 - utx1
				val ch = uty2 - uty1
				
				if (uvx1 < x1) {
					uvx1 = x1
					val p = (x1 - v1.x) / ow
					val adjust = p * cw
					utx1 += adjust
				}
				if (uvy1 < y1) {
					uvy1 = y1
					val p = (y1 - v1.y) / oh
					val adjust = p * ch
					uty1 += adjust
				}
				if (uvx2 > x2) {
					uvx2 = x2
					val p = (v2.x - x2) / ow
					val adjust = cw - (p * cw)
					utx2 = utx1 + adjust
				}
				if (uvy2 > y2) {
					uvy2 = y2
					val p = (v3.y - y2) / oh
					val adjust = ch - (p * ch)
					uty2 = uty1 + adjust
				}
				
				vertices = Vec3d(uvx1, uvy1, v1.z) :: vertices
				vertices = Vec3d(uvx2, uvy1, v2.z) :: vertices
				vertices = Vec3d(uvx2, uvy2, v3.z) :: vertices
				vertices = Vec3d(uvx1, uvy2, v4.z) :: vertices
				
				if (tl != Nil) {
					texcoords = Vec2d(utx1, uty1) :: texcoords
					texcoords = Vec2d(utx2, uty1) :: texcoords
					texcoords = Vec2d(utx2, uty2) :: texcoords
					texcoords = Vec2d(utx1, uty2) :: texcoords
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
		} else if ((v.y < y1) || (v.y > y2)) {
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