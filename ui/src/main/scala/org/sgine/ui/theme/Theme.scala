package org.sgine.ui.theme

import org.sgine.property.{MutableProperty, StandardProperty, PropertyParent, Property}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Theme extends StandardProperty[Theme]("Theme")(null) with PropertyParent {
  this := WindowsTheme

  def apply(property: ThemableProperty[_]) = {
    value match {
      case null =>                        // Do nothing - no theme is active
      case theme => {
        theme.updateProperty(property)    // Update via properties
        theme(property)
      }
    }
  }
}

/**
 * Theme instances utilize child properties during creation of Components to update values.
 */
class Theme(val name: String) extends PropertyParent {
  val parent = Theme

  private def updateProperty(property: ThemableProperty[_], themeProperties: Seq[Property[_]] = properties): Unit = {
    if (themeProperties.nonEmpty) {
      val themeProperty = themeProperties.head
      if (property.hierarchicalMatch(themeProperty.name)) {
        property.asInstanceOf[MutableProperty[Any]] := themeProperty().asInstanceOf[Any]
      } else {
        updateProperty(property, themeProperties.tail)
      }
    }
  }

  /**
   * Can be overridden to apply values to a Property during the creation process directly.
   */
  protected def apply(property: Property[_]) = {
  }
}