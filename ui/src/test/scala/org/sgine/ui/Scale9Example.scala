package org.sgine.ui

import org.powerscala.Resource

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Scale9Example extends UI {
  val scale9 = Scale9(Resource("scale9test.png"), 50.0, 50.0, 450.0, 450.0)
  scale9.size.width := 200.0
  contents += scale9
}