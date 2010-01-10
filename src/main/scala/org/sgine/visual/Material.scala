package org.sgine.visual

import org.sgine.visual.material._

trait Material {
	def pigment: Pigment
	def normal: Normal
	def finish: Finish
}