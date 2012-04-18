package org.sgine.ui.theme

import org.sgine.property.MutableProperty


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait ThemableProperty[T] extends MutableProperty[T] {
  override protected def initialize() = {
    super.initialize()
    Theme(this)
  }
}
