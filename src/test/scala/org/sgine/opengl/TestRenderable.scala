package org.sgine.opengl

import org.sgine.math.mutable.Vector2
import org.sgine.math.Matrix4
import org.sgine.math.VertexBuffer
import org.sgine.core.Color
import org.sgine.opengl.renderable._
import org.sgine.opengl.state.TranslateState
import org.sgine.math.Vector3;

import javax.imageio._

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
		val vertices1 = VertexBuffer(3, 6)
		vertices1.set(0, -50.0f, -50.0f, 0.0f)
		vertices1.set(1, 50.0f, -50.0f, 0.0f)
		vertices1.set(2, 50.0f, 50.0f, 0.0f)
		vertices1.set(3, -50.0f, 50.0f, 0.0f)
		vertices1.set(4, -50.0f, -50.0f, 0.0f)
		vertices1.set(5, 50.0f, 50.0f, 0.0f)
		s.renderItems(2) = new ShapeRenderItem(vertices1)					// Draw
		w.displayables.add(s)
		
		// Use BasicRenderable
		val r = BasicRenderable(6)
		r.matrixItem.matrix = Matrix4.Identity.translateZ(-900.0).translateX(50.0).rotate(0.0, 0.0, Math.Pi / 4.0)
		r.colorItem = new ColorRenderItem(Color.White.subtract(alpha = 0.5))
		val vertices2 = VertexBuffer(3, 6)
		vertices2.set(0, -50.0f, -50.0f, 0.0f)
		vertices2.set(1, 50.0f, -50.0f, 0.0f)
		vertices2.set(2, 50.0f, 50.0f, 0.0f)
		vertices2.set(3, -50.0f, 50.0f, 0.0f)
		vertices2.set(4, -50.0f, -50.0f, 0.0f)
		vertices2.set(5, 50.0f, 50.0f, 0.0f)
		r.shapeItem = new ShapeRenderItem(vertices2)
		
		// Texture coordinates
		val coords = new Array[Vector2](6)
		coords(0) = Vector2.UnitY
		coords(1) = Vector2.Ones
		coords(2) = Vector2.UnitX
		coords(3) = Vector2.Zero
		coords(4) = Vector2.UnitY
		coords(5) = Vector2.UnitX
		r.textureCoordinatesItem = new TextureCoordinatesRenderItem(coords)
		
		// Texture
		val t = new Texture()
		val image = ImageIO.read(classOf[GLWindow].getClassLoader().getResource("resource/puppies.jpg"))
		t(image, 0, 0, image.getWidth, image.getHeight, true)
		r.textureItem = new TextureRenderItem(t)
		
		w.displayables.add(r)
		
		// Add an FPS counter to see how fast we're going
		w.displayables.add(FPS());
		
		// Start / Display the window
		w.begin();
	}
}