package org.sgine.opengl

import org.sgine.math.Matrix4
import org.sgine.core.Color
import org.sgine.opengl.renderable._
import org.sgine.opengl.state.TranslateState
import org.sgine.math.Vector3;

object TestRenderable {
	def main(args: Array[String]): Unit = {
		// Create our window
		val w = GLWindow("Test Window", 1024, 768);
		w.verticalSync := false
		
		// Create a quad shape
		val s = new MutableRenderable(3, 6)
		val m = Matrix4.Identity.translateZ(-1000.0)
		s.renderItems(0) = new MatrixRenderItem(m)							// Position
		s.renderItems(1) = new ColorRenderItem(Color.Blue)					// Colorize
		s.renderItems(2) = new ShapeRenderItem()							// Draw
		s.vertices(0) = Vector3(-50.0, -50.0, 0.0)
		s.vertices(1) = Vector3(50.0, -50.0, 0.0)
		s.vertices(2) = Vector3(50.0, 50.0, 0.0)
		s.vertices(3) = Vector3(-50.0, 50.0, 0.0)
		s.vertices(4) = Vector3(-50.0, -50.0, 0.0)
		s.vertices(5) = Vector3(50.0, 50.0, 0.0)
		w.displayables.add(s)
		
		// Use BasicRenderable
		val r = BasicRenderable(6)
		r.matrixItem.matrix = Matrix4.Identity.translateZ(-900.0).translateX(50.0).rotate(0.0, 0.0, Math.Pi / 4.0)
		r.colorItem = new ColorRenderItem(Color.Red.subtract(alpha = 0.5))
		r.vertices(0) = Vector3(-50.0, -50.0, 0.0)
		r.vertices(1) = Vector3(50.0, -50.0, 0.0)
		r.vertices(2) = Vector3(50.0, 50.0, 0.0)
		r.vertices(3) = Vector3(-50.0, 50.0, 0.0)
		r.vertices(4) = Vector3(-50.0, -50.0, 0.0)
		r.vertices(5) = Vector3(50.0, 50.0, 0.0)
		w.displayables.add(r)
		
		// Add an FPS counter to see how fast we're going
		w.displayables.add(FPS());
		
		// Start / Display the window
		w.begin();
	}
}