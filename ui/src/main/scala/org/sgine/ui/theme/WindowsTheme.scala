package org.sgine.ui.theme

import org.sgine.property.Property
import org.sgine.ui.font.BitmapFont
import org.sgine.Resource

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object WindowsTheme extends Theme("windows") {
  val font = Property[BitmapFont]("_font", BitmapFont(Resource("arial64.fnt")))
  val resource = Property[Resource]("Button.background.resource", Resource("scale9/windows/button/normal.png"))
}