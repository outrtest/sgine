package org.sgine.ui


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ResolutionExample extends UI {
  perspective()

  val image640 = Image("640.jpg")
  image640.alpha := 0.5
  image640.resolution(640.0, 480.0)
  contents += image640

  val image1024 = Image("1024.jpg")
  image1024.alpha := 0.5
  image1024.resolution(1024.0, 768.0)
  contents += image1024

  val image200 = Image("200.jpg")
  image200.alpha := 0.5
  image200.resolution(200.0, 600.0)
  contents += image200
}
