package org.sgine.ui

import org.lwjgl.opengl.GL11._
import org.lwjgl.util.glu.tessellation._

import org.sgine.render._

import org.sgine.ui.ext._

object TestTesselate extends StandardDisplay {
	override val settings = RenderSettings.High
	
	private var count = 0
	
	def setup() = {
		val c = new AdvancedComponent() {
			private lazy val tess = generateTess()
			
			private def generateTess() = {
				val tess = GLUtessellatorImpl.gluNewTess()
				
				val tessCb = new org.lwjgl.util.glu.GLUtessellatorCallbackAdapter() {
					override def begin(t: Int) = {
//						println("begin " + t)
						glBegin(t)
					}
					
					override def vertex(vertexData: AnyRef) = {
//						println("vertex")
						val vert = vertexData.asInstanceOf[Array[Double]]
						glVertex2d(vert(0), vert(1))
						count += 1
					}
					
					override def combine(coords: Array[Double], data: Array[AnyRef], weight: Array[Float], outData: Array[AnyRef]) = {
//						println("combine " + coords.length + ", " + data + ", " + weight + ", " + outData)
						for (i <- 0 until outData.length) {
							val combined = new Array[Double](6)
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
				count = 0
				tess.gluBeginPolygon()
				
				val trans = new java.awt.geom.AffineTransform()
//			    trans.translate(-200, 0)
			    trans.scale(1, -1)
				val font = new java.awt.Font("Dialog", java.awt.Font.BOLD, 150).deriveFont(trans)
			    val fontRenderContext = new java.awt.font.FontRenderContext(null, true, true)
			    val glyphVector = font.createGlyphVector(fontRenderContext, "Sgine!")
				val shape = glyphVector.getOutline
				val path = new java.awt.geom.FlatteningPathIterator(shape.getPathIterator(null), 0.001, 10)
				
				val coords = Array(0f, 0f)
				while (! path.isDone) {
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
			      path.next
			    }
				
//				val star1 = Array(250.0, 50.0, 0.0, 1.0, 0.0, 1.0)
//				val star2 = Array(325.0, 200.0, 0.0, 1.0, 1.0, 0.0)
//				val star3 = Array(400.0, 50.0, 0.0, 0.0, 1.0, 1.0)
//				val star4 = Array(250.0, 150.0, 0.0, 1.0, 0.0, 0.0)
//				val star5 = Array(400.0, 150.0, 0.0, 0.0, 1.0, 0.0)
//				tess.gluTessVertex(star1, 0, star1)
//				tess.gluTessVertex(star2, 0, star2)
//				tess.gluTessVertex(star3, 0, star3)
//				tess.gluTessVertex(star4, 0, star4)
//				tess.gluTessVertex(star5, 0, star5)
				
//				tess.gluTessVertex(Array(50.0f, 50.0f, 0.0f), 0, Array(50.0f, 50.0f, 0.0f))
//				tess.gluTessVertex(Array(200.0f, 50.0f, 0.0f), 0, Array(200.0f, 50.0f, 0.0f))
//				tess.gluTessVertex(Array(200.0f, 200.0f, 0.0f), 0, Array(200.0f, 200.0f, 0.0f))
//				tess.gluTessVertex(Array(50.0f, 200.0f, 0.0f), 0, Array(50.0f, 200.0f, 0.0f))
//				tess.gluTessEndContour()
				tess.gluEndPolygon()
				
				println("Count: " + count)
			}
		}
		
		scene += c
	}
}