package org.sgine.ui

import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.Gdx

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object RotationExample extends UI {
  println("setting false...")
  verticalSync := false

  lazy val framerate = new FPSLogger()

  val image = new Image("sgine.png") {
    override protected def draw() = {
      super.draw()

      rotation.z := rotation.z() + (200.0 * Gdx.graphics.getDeltaTime)

      framerate.log()
    }
  }
  contents += image
}
