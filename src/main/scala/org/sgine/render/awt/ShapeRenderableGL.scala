package org.sgine.render.awt

import java.awt.geom.PathIterator._

import org.lwjgl.opengl.GL11._

import org.sgine.render.Graphics3D

class ShapeRenderableGL(val shape: java.awt.Shape) extends ShapeRenderable {
	val store = new Array[Double](6)
	
	def apply() = {		// TODO: finish implementation
		val iterator = shape.getPathIterator(null)
		
		var x = 0.0
		var y = 0.0
		while (!iterator.isDone) {
			iterator.currentSegment(store) match {
				case SEG_MOVETO => {
					x = store(0)
					y = store(1)
				}
				case SEG_LINETO => {
					Graphics3D.drawLine(x, y, store(0), store(1))
					x = store(0)
					y = store(1)
				}
				case SEG_QUADTO => println("Quad: " + store(0) + ", " + store(1) + ", " + store(2) + ", " + store(3) + ", " + store(4) + ", " + store(5))
				case SEG_CUBICTO => println("Cubic: " + store(0) + ", " + store(1) + ", " + store(2) + ", " + store(3) + ", " + store(4) + ", " + store(5))
				case SEG_CLOSE => 
			}
			
			iterator.next()
		}
	}
}