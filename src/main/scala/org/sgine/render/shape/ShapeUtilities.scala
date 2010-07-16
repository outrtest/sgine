package org.sgine.render.shape

import java.awt.geom.AffineTransform
import java.awt.geom.FlatteningPathIterator

import org.lwjgl.util.glu._
import org.lwjgl.util.glu.tessellation._

import scala.collection.mutable.ArrayBuffer

import simplex3d.math.doublem._

object ShapeUtilities {
	def convert(awtShape: java.awt.Shape) = {
		val tess = GLUtessellatorImpl.gluNewTess()
		
		val cb = new TessCallback()
		tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_BEGIN, cb)
		tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_VERTEX, cb)
		tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_COMBINE, cb)
		tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_END, cb)
		
		tess.gluBeginPolygon()
		
		val transform = new AffineTransform()
		transform.scale(1.0, -1.0)
		
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
		
		// TODO: multiple shapes....doh
//		val data = ArrayShapeData(cb.glType, )
//		data
	}
}

class TessCallback extends GLUtessellatorCallbackAdapter {
	var glType: Int = _
	val buffer = new ArrayBuffer[Vec3d]
	
	override def begin(t: Int) = {
		glType = t
	}
	
	override def vertex(vertexData: AnyRef) = {
		val data = vertexData.asInstanceOf[Array[Double]]
		buffer += Vec3d(data(0), data(1), 0.0)
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