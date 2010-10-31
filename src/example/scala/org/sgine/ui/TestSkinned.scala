package org.sgine.ui

import org.sgine.core._

import org.sgine.property.animate.LinearNumericAnimator

import org.sgine.render.Debug
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay

import org.sgine.ui.skin.Scale9Skin

import org.sgine.work.DefaultWorkManager

object TestSkinned extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val skinned = SkinnedComponent()
		skinned.size(500.0, 500.0)
		skinned.size.width.mode := "explicit"
		skinned.size.height.mode := "explicit"
		
		val scale9 = new Scale9Skin()
		scale9(Resource("scale9test.png"), 50.0, 50.0, 450.0, 450.0)
		
		skinned.skin := scale9
		
		scene += skinned
		
		skinned.size.width.animator = new LinearNumericAnimator(500.0)
		skinned.size.height.animator = new LinearNumericAnimator(500.0)
		
		DefaultWorkManager += (() => {
			while (true) {
				skinned.size.width := 1000.0
				skinned.size.width.waitForTarget()
				skinned.size.height := 200.0
				skinned.size.height.waitForTarget()
				skinned.size.width := 500.0
				skinned.size.width.waitForTarget()
				skinned.size.height := 500.0
				skinned.size.height.waitForTarget()
			}
		})
	}
}