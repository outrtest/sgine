package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource
import org.sgine.core.mutable.{Color => MutableColor}

import org.sgine.easing.Elastic

import org.sgine.effect._

import org.sgine.property.animate.EasingNumericAnimator
import org.sgine.property.animate.LinearNumericAnimator

import org.sgine.render.Debug
import org.sgine.render.Renderer
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.MutableNodeContainer

import scala.math._

object TestStressCubes extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		for (row <- 0 until 20) {
			for (column <- 0 until 20) {
				CubeCreator.createCube(scene, column, row, 0.1)
			}
		}
	}
	
	private def test(evt: org.sgine.event.Event) = {
		println(evt)
	}
}