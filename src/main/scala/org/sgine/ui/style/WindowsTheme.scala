package org.sgine.ui.style

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.font.FontManager

object WindowsTheme extends Theme {
	val buttonNormalSkin = Resource("scale9/windows/button/normal.png")
	val buttonHoverSkin = Resource("scale9/windows/button/hover.png")
	val buttonPressedSkin = Resource("scale9/windows/button/pressed.png")
	val buttonFocusSkin = Resource("scale9/windows/button/focused.png")
	val buttonSkinX1 = 3.0
	val buttonSkinY1 = 3.0
	val buttonSkinX2 = 4.0
	val buttonSkinY2 = 5.0
	val buttonPaddingTop = 10.0
	val buttonPaddingBottom = 10.0
	val buttonPaddingLeft = 25.0
	val buttonPaddingRight = 25.0
	
	val font = FontManager("Arial32")
	val textColor = Color.Black
}