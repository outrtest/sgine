package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Graphics3D._
import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.WorldMatrixNode
import org.sgine.scene.ext.ResolutionNode

import org.sgine.ui.ext._

import org.lwjgl._
import org.lwjgl.opengl._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._

import scala.math._

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

object TestTTF {
	private val font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 32)
	private val gv = font.createGlyphVector(new java.awt.font.FontRenderContext(font.getTransform(), true, false), "Hello World!")
	private val glyphChar = 'H'
	private val glyphBounds = gv.getGlyphOutline(0).getBounds2D()
	private val glyph = gv.getGlyphOutline(0, -glyphBounds.getX.toFloat, -glyphBounds.getY.toFloat)
	private val pi = glyph.getPathIterator(null)
	
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test TTF", 4, 8, 4, 4)
		r.verticalSync := false
		
		val scene = new GeneralNodeContainer() with WorldMatrixNode
		val m = scene.worldMatrix()
		m := m translate(Vec3(0.0, 0.0, -200.0))
		
		scene += new AdvancedComponent() {
			def drawComponent() = {
				val tess = org.lwjgl.util.glu.tessellation.GLUtessellatorImpl.gluNewTess()
				val tessCb = new org.lwjgl.util.glu.GLUtessellatorCallbackAdapter() {
					override def begin(t: Int) = {
						glBegin(t)
					}
					
					override def vertex(vertexData: AnyRef) = {
						val vert = vertexData.asInstanceOf[Array[Float]]
						glVertex2f(vert(0), vert(1))
					}
					
					override def combine(coords: Array[Double], data: Array[AnyRef], weight: Array[Float], outData: Array[AnyRef]) = {
						for (i <- 0 until outData.length) {
							val combined = new Array[Float](6)
							combined(0) = coords(0).toFloat
							combined(1) = coords(1).toFloat
							outData(i) = combined
						}
					}
					
					override def end() = {
						glEnd()
					}
				}
				tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_BEGIN, tessCb)
				tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_VERTEX, tessCb)
				tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_COMBINE, tessCb)
				tess.gluTessCallback(org.lwjgl.util.glu.GLU.GLU_TESS_END, tessCb)
				val current = new java.awt.geom.Point2D.Float()
				tess.gluTessBeginPolygon(null)
				while (!pi.isDone) {
					val coords = new Array[Float](6)
					val path = pi.currentSegment(coords)
					pi.getWindingRule match {
						case java.awt.geom.PathIterator.WIND_EVEN_ODD => tess.gluTessProperty(org.lwjgl.util.glu.GLU.GLU_TESS_WINDING_RULE, org.lwjgl.util.glu.GLU.GLU_TESS_WINDING_ODD)
						case java.awt.geom.PathIterator.WIND_NON_ZERO => tess.gluTessProperty(org.lwjgl.util.glu.GLU.GLU_TESS_WINDING_RULE, org.lwjgl.util.glu.GLU.GLU_TESS_WINDING_NONZERO)
						case _ => println("*** ERROR!")
					}
					path match {
						case java.awt.geom.PathIterator.SEG_MOVETO => {
							tess.gluTessBeginContour()
							tess.gluTessVertex(Array(coords(0), coords(1), 0.0), 0, Array(coords(0), coords(1), 0.0f))
							current.x = coords(0)
							current.y = coords(1)
						}
						case java.awt.geom.PathIterator.SEG_CLOSE => tess.gluTessEndContour()
						case java.awt.geom.PathIterator.SEG_LINETO => {
							tess.gluTessVertex(Array(coords(0), coords(1), 0.0), 0, Array(coords(0), coords(1), 0.0f))
							current.x = coords(0)
							current.y = coords(1)
						}
						case java.awt.geom.PathIterator.SEG_CUBICTO => {
//							for (p <- )
						}
					}
				}
			}
		}
		
		r.renderable := RenderableScene(scene)
	}
}