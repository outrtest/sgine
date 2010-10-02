package org.sgine.render.shape

import java.awt.geom.AffineTransform
import java.awt.geom.FlatteningPathIterator

import org.lwjgl.opengl.GL11._
import org.lwjgl.util.glu._
import org.lwjgl.util.glu.tessellation._

import scala.collection.mutable.ArrayBuffer

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