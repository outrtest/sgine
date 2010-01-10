package org.sgine.visual

import org.sgine.event._
import org.sgine.property._

trait Shape extends Listenable {
	def mesh: Mesh
	def material: Material
}