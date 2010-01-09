package org.sgine.visual

import org.sgine.event._
import org.sgine.property._

abstract class Shape extends Listenable {
	var mesh: Mesh
	val material: Material
}