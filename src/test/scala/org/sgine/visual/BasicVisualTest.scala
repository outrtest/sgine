package org.sgine.visual

import org.sgine.visual.material.pigment._

object BasicVisualTest {
	def main(args: Array[String]) = {
		val w = Window("Simple Example")
		
		val blue = Color.Blue
		val blueMaterial = ColoredPigment(blue)
//		val quad = Quad(200.0, 100.0, blueMaterial)
//		w.scene += quad
		
		w.start()
	}
}