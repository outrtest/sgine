package org.sgine.ui

import org.lwjgl.opengl.GL11._
import org.lwjgl.util.glu.tessellation._

import org.sgine.render._

import org.sgine.ui.ext._

object TestTesselate extends StandardDisplay {
	def setup() = {
		val c = new AdvancedComponent() {
			private lazy val tess = generateTess()
			
			private def generateTess() = {
				val tess = GLUtessellatorImpl.gluNewTess()
				
				val tessCb = new org.lwjgl.util.glu.GLUtessellatorCallbackAdapter() {
					override def begin(t: Int) = {
//						println("begin")
						glBegin(t)
					}
					
					override def vertex(vertexData: AnyRef) = {
//						println("vertex")
						val vert = vertexData.asInstanceOf[Array[Float]]
						glVertex2f(vert(0), vert(1))
					}
					
					override def combine(coords: Array[Double], data: Array[AnyRef], weight: Array[Float], outData: Array[AnyRef]) = {
//						println("combine")
						for (i <- 0 until outData.length) {
							val combined = new Array[Float](6)
							combined(0) = coords(0).toFloat
							combined(1) = coords(1).toFloat
							outData(i) = combined
						}
					}
					
					override def end() = {
//						println("end")
						glEnd()
					}
				}
				
				tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_BEGIN, tessCb)
				tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_VERTEX, tessCb)
				tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_COMBINE, tessCb)
				tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_END, tessCb)
				
				tess
			}
			
			def drawComponent() = {
				tess.gluBeginPolygon()
				tess.gluTessBeginContour()
				tess.gluTessVertex(Array(50.0f, 50.0f, 0.0f), 0, Array(50.0f, 50.0f, 0.0f))
				tess.gluTessVertex(Array(200.0f, 50.0f, 0.0f), 0, Array(200.0f, 50.0f, 0.0f))
				tess.gluTessVertex(Array(200.0f, 200.0f, 0.0f), 0, Array(200.0f, 200.0f, 0.0f))
				tess.gluTessVertex(Array(50.0f, 200.0f, 0.0f), 0, Array(50.0f, 200.0f, 0.0f))
				tess.gluTessEndContour()
				tess.gluEndPolygon()
			}
		}
		
		scene += c
	}
}