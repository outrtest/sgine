package org.sgine.visual.transform

import org.sgine.property._
import org.sgine.property.container._

import simplex3d.math._
import simplex3d.math.doublem._

trait Transform extends PropertyContainer {
	val rotation = new AdvancedProperty[Vec3d](Vec3d.Zero, this)
	val scale = new AdvancedProperty[Vec3d](Vec3d.Zero, this)
	val translation = new AdvancedProperty[Vec3d](Vec3d.Zero, this)
}