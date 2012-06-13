package org.sgine.ui


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object MediaExample extends UI {
  width := 1920
  height := 1080
  fullscreen := true

  perspective()
  resolution(1920.0, 1080.0, false)

  val media = new Media()
  media.size.algorithm := SizeAlgorithm.aspectRatio(1024.0, 768.0)
  media.buffers := 1
  media.resource := "trailer_iphone.m4v"
  contents += media

  media.play()
}