package org.sgine.ui.style

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.font.Font

trait Theme {
	def buttonNormalSkin: Resource
	def buttonHoverSkin: Resource
	def buttonPressedSkin: Resource
	def buttonFocusSkin: Resource
	def buttonSkinX1: Double
	def buttonSkinY1: Double
	def buttonSkinX2: Double
	def buttonSkinY2: Double
	def buttonPaddingTop: Double
	def buttonPaddingBottom: Double
	def buttonPaddingLeft: Double
	def buttonPaddingRight: Double
	
	def font: Font
	def textColor: Color
}

object Theme {
	var current: Theme = WindowsTheme
}