package org.sgine.visual

import org.sgine.visual.material._

trait Material {
	val pigment: Pigment
	val normal: Normal
	val finish: Finish
}