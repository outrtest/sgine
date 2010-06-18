package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Direction
import org.sgine.core.Placement
import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.event._

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.font.Font
import org.sgine.render.font.FontManager

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.PaddingComponent
import org.sgine.ui.layout.BoxLayout
import org.sgine.ui.style.Theme

class Button extends SkinnableComponent with AdvancedComponent with PaddingComponent {
	protected val normalResource = Button.normalSkin()
	protected val hoverResource = Button.hoverSkin()
	protected val pressedResource = Button.pressedSkin()
	protected val focusedResource = Button.focusedSkin()

	private var pressed: Boolean = false
	private var over: Boolean = false
	
	protected val face = new Container()
	private val image = new Image()
	face += image
	private val label = new Label()
	face += label
	
	protected val skinX1 = Button.skinX1()
	protected val skinY1 = Button.skinY1()
	protected val skinX2 = Button.skinX2()
	protected val skinY2 = Button.skinY2()
	
	padding.top := Button.paddingTop()
	padding.bottom := Button.paddingBottom()
	padding.left := Button.paddingLeft()
	padding.right := Button.paddingRight()
	
	val font = new AdvancedProperty[Font](null, this, null, Theme.font)
	val text = new AdvancedProperty[String]("", this)
	val textColor = new AdvancedProperty[Color](null, this, null, Theme.textColor)
	val icon = new AdvancedProperty[Resource](null, this)
	
	val iconPlacement = new AdvancedProperty[Placement](null, this, null, Button.iconPlacement)
	val iconSpacing = new AdvancedProperty[Double](0.0, this, null, Button.iconSpacing)
	
	configureBindings()
	configureListeners()
	
	def this(text: String) = {
		this()
		
		this.text := text
	}
	
	protected def configureBindings() = {
		face.location.z := 0.00001
		
		generateLayout()
		
		label.font bind font
		label.text bind text
		label.color bind textColor
		
		image.source bind icon
	}
	
	private def generateLayout(evt: PropertyChangeEvent[_] = null) = {
		if (iconPlacement() == Placement.Middle) {
			face.layout := null
		} else {
			val direction = iconPlacement() match {
				case Placement.Top => Direction.Vertical
				case Placement.Bottom => Direction.Vertical
				case Placement.Left => Direction.Horizontal
				case Placement.Right => Direction.Horizontal
			}
			val spacing = iconSpacing()
			val reverse = iconPlacement() match {
				case Placement.Bottom => true
				case Placement.Right => true
				case _ => false
			}
			face.layout := BoxLayout(direction, spacing, reverse)
		}
	}
	
	private def configureListeners() = {
		face.listeners += EventHandler(updateBounding, ProcessingMode.Blocking)
		
		iconPlacement.listeners += EventHandler(generateLayout, ProcessingMode.Blocking)
		iconSpacing.listeners += EventHandler(generateLayout, ProcessingMode.Blocking)
	}
}

object Button extends PropertyContainer with ListenableProperty[Int] {
	val normalSkin = new AdvancedProperty[Resource](null, this)
	val hoverSkin = new AdvancedProperty[Resource](null, this)
	val pressedSkin = new AdvancedProperty[Resource](null, this)
	val focusedSkin = new AdvancedProperty[Resource](null, this)
	
	val skinX1 = new AdvancedProperty[Double](0.0, this)
	val skinY1 = new AdvancedProperty[Double](0.0, this)
	val skinX2 = new AdvancedProperty[Double](0.0, this)
	val skinY2 = new AdvancedProperty[Double](0.0, this)
	
	val paddingTop = new AdvancedProperty[Double](0.0, this)
	val paddingBottom = new AdvancedProperty[Double](0.0, this)
	val paddingLeft = new AdvancedProperty[Double](0.0, this)
	val paddingRight = new AdvancedProperty[Double](0.0, this)
	
	val iconPlacement = new AdvancedProperty[Placement](null, this)
	val iconSpacing = new AdvancedProperty[Double](5.0, this)
	
	parent = Theme
}