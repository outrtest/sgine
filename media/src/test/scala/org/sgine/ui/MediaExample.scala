package org.sgine.ui


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object MediaExample extends UI {
  val media = new Media()
  media.size.algorithm := SizeAlgorithm.aspectRatio(1024.0, 768.0)
  media.buffers := 3
  media.resource := "trailer_iphone.m4v"
  contents += media

  media.play()
}