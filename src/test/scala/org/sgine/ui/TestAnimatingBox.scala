package org.sgine.ui

import org.sgine.property.animate.{EasingColorAnimator, EasingNumericAnimator}
import org.sgine.core.{Color, Resource}
import org.sgine.easing.Back
import org.sgine.render.{Debug, TextureManager, RenderImage, StandardDisplay}
import org.sgine.work.Updatable

import scala.math.random
import scala.math.Pi

/*
 * Demonstrates the Box and using its properties with adjusters.
 */
object TestAnimatingBox extends StandardDisplay with Debug with Updatable {

  private var box: Box = new Box()

  private val animationTimeSeconds = 2.0;

  private var countdown = 0.0;

  def setup() {
    // Add box to scene
    scene +=  box

    // Set texture for box
    box.texture := RenderImage(TextureManager(Resource("sgine_256.png")))

    // Setup adjusters for the box size, rotation, and color
    val easingMethod = Back.easeInOut _  // Back.easeInOut will change a value by backing off in the other direction to gather speed, leap to the target value and a bit over, and back to the target.
    val adjustTime = animationTimeSeconds // How long it should take for the easing method to change the value

    box.width.adjuster = new EasingNumericAnimator(easingMethod, adjustTime)
    box.height.adjuster = new EasingNumericAnimator(easingMethod, adjustTime)
    box.depth.adjuster = new EasingNumericAnimator(easingMethod, adjustTime)

    box.rotation.x.adjuster = new EasingNumericAnimator(easingMethod, adjustTime)
    box.rotation.y.adjuster = new EasingNumericAnimator(easingMethod, adjustTime)
    box.rotation.z.adjuster = new EasingNumericAnimator(easingMethod, adjustTime)

    box.color.adjuster = new EasingColorAnimator(easingMethod, adjustTime)

    // Tell Updatable to start calling update
    initUpdatable()
  }

  // Provided by the Updatable trait, called regularly
  override def update(timeSinceLastCallInSeconds: Double) {

    // Every animationTimeSeconds we specify new values for the box properties
    countdown -= timeSinceLastCallInSeconds
    if (countdown <= 0) {
      countdown = animationTimeSeconds

      setNewBoxTargets()
    }
  }
  
  def setNewBoxTargets() {
    // Set new values for box size, angle and color.
    // Because we specified adjusters, the values will be animated from their current value to the new value automatically.
    box.width := random * 500
    box.height := random * 500
    box.depth := random * 500

    box.rotation.x := random * 2 * Pi
    box.rotation.y := random * 2 * Pi
    box.rotation.z := random * 2 * Pi

    box.color := Color(random, random, random, 1)
  }

}