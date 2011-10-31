package org.sgine.ui

import org.sgine.Resource

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object UITest extends UI {
  val image = new Image()
  image.load(Resource("backdrop_mountains.jpg"))
  contents += image
}