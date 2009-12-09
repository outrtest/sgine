package org.sgine.opengl.scene

import org.sgine.easing._
import org.sgine.opengl._
import org.sgine.opengl.scene._
import org.sgine.opengl.shape._
import org.sgine.property._
import org.sgine.property.adjust._

import javax.imageio._

object TestGLScene {
	def main(args: Array[String]): Unit = {
		// Create the GLWindow to render to
		val w = new GLWindow("Test GL Scene", 1024, 768)
		
		// Create a container for our scene
		val root = new GLNodeContainer()
		val node = new GLShape()
		root.rotation.z.adjuster = new EasingNumericAdjuster(Linear.easeOut, 10.0)
		root.rotation.z := 3.0
		node.location.z := -1000.0
		node.location.z.adjuster = new EasingNumericAdjuster(Bounce.easeOut, 5.0)
		node.location.z := -2000.0
		node.location.z.listeners += zChanged _
		root += node
		val shape = Shape(ImageIO.read(classOf[GLWindow].getClassLoader().getResource("resource/puppies.jpg")))
		node.shape := shape
		
		val renderer = new GLSceneRenderer(root)
		w.displayables.add(renderer)
		
		w.displayables.add(FPS())
		
		w.start()
		
		node.location.z.waitForTarget()
		node.location.z := -500.0
		node.location.z.waitForTarget()
	}
	
	private def zChanged(evt: PropertyChangeEvent[Double]) = {
//		println(evt.newValue)
	}
}