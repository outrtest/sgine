package org.sgine.ui.layout

import org.sgine.ui.AbstractContainer

/**
 * LayoutContainer makes the layout property visible so it can be dynamically modified.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait LayoutableContainer extends AbstractContainer {
  val layout = _layout
}
