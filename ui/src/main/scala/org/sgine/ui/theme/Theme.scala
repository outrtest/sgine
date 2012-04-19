package org.sgine.ui.theme

import org.sgine.property.{MutableProperty, StandardProperty, PropertyParent, Property}
import org.sgine.ui.Component
import annotation.tailrec

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Theme extends StandardProperty[Theme]("Theme")(null) with PropertyParent {
  this := WindowsTheme

  def apply(component: Component) = {
    value match {
      case null =>                                                     // Do nothing - no theme is active
      case theme => {
        theme.updateProperties(component.allProperties)                // Update properties
        theme(component)
      }
    }
  }

  def apply(property: MutableProperty[_]) = {
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

  @tailrec
  private def updateProperties(properties: Seq[Property[_]]): Unit = {
    if (properties.nonEmpty) {
      val property = properties.head
      property match {
        case mutable: MutableProperty[_] => updateProperty(mutable)
        case _ => // Not mutable
      }
      apply(property)
      updateProperties(properties.tail)
    }
  }

  private def updateProperty(property: MutableProperty[_], themeProperties: Seq[Property[_]] = allProperties): Unit = {
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
   * Can be overridden to apply values to a Component during the theming process.
   */
  protected def apply(component: Component) = {
  }

  /**
   * Can be overridden to apply values to a Property during the theming process.
   */
  protected def apply(property: Property[_]) = {
  }
}