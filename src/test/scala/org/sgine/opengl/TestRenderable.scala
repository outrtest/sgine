package org.sgine.opengl

import org.sgine.opengl.renderable.ShapeRenderable
import org.sgine.opengl.state.TranslateState
import org.sgine.math.Vector3;

object TestRenderable {
	def main(args: Array[String]): Unit = {
		// Create our window
		val w = GLWindow("Test Window", 1024, 768);
		
		// Translate back so we can see
		w.displayables.add(TranslateState(0.0, 0.0, -1000.0));
		
		// Create a quad shape with a texture and add it
//		val s = Shape(ImageIO.read(classOf[GLWindow].getClassLoader().getResource("resource/puppies.jpg")));
//		w.displayables.add(s);
		
		val s = new TestRenderable()
		w.displayables.add(s)
		
		// Add an FPS counter to see how fast we're going
		w.displayables.add(FPS());
		
		// Start / Display the window
		w.begin();
	}
}

class TestRenderable extends ShapeRenderable {
	val points = Vector3(-50.0, -50.0, 0.0) ::
				 Vector3(50.0, -50.0, 0.0) ::
				 Vector3(50.0, 50.0, 0.0) ::
				 Vector3(-50.0, 50.0, 0.0) ::
				 Vector3(-50.0, -50.0, 0.0) ::
				 Vector3(50.0, 50.0, 0.0) ::
				 Nil
}