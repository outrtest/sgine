package org.sgine.visual.transform

import org.sgine.math._

trait Transform {
	def rotation: Vector3
	def scale: Vector3
	def translation: Vector3
}