package org.sgine.ui.style

import org.sgine.ui.Component

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Stylized extends Component {
  val style = new Style(this) with MouseStyle
}
