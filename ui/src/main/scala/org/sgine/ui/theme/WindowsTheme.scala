package org.sgine.ui.theme

import org.sgine.ui.font.BitmapFont
import org.sgine.property.Property
import org.sgine.ui.{Button, Component}
import org.sgine.{Color, Resource}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object WindowsTheme extends Theme("windows") {
  val font = Property[BitmapFont]("_font", BitmapFont(Resource("arial18.fnt")))

  val buttonResource = Property[Resource]("Button.background.resource", Resource("scale9/windows/button/normal.png"))
  val buttonSliceX1 = Property[Double]("Button.background.slice.x1", 3.0)
  val buttonSliceY1 = Property[Double]("Button.background.slice.y1", 3.0)
  val buttonSliceX2 = Property[Double]("Button.background.slice.x2", 4.0)
  val buttonSliceY2 = Property[Double]("Button.background.slice.y2", 5.0)
  val buttonPaddingTop = Property[Double]("Button.padding.top", 5.0)
  val buttonPaddingBottom = Property[Double]("Button.padding.bottom", 5.0)
  val buttonPaddingLeft = Property[Double]("Button.padding.left", 5.0)
  val buttonPaddingRight = Property[Double]("Button.padding.right", 5.0)
  val hoverStyle: Component => Any = {
    case button: Button => button.background.resource := Resource("scale9/windows/button/hover.png")
  }
  val buttonHoverStyle = Property[Component => Any]("Button.style.mouseOver", hoverStyle)
  val pressedStyle: Component => Any = {
    case button: Button => button.background.resource := Resource("scale9/windows/button/pressed.png")
  }
  val buttonPressedStyle = Property[Component => Any]("Button.style.mousePress", pressedStyle)

  override protected def apply(component: Component) = {
    super.apply(component)

    component match {
      case button: Button => {
        button.label.color(Color.Black)
      }
      case _ =>
    }
  }
}