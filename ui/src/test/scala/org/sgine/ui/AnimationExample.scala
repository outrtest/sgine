package org.sgine.ui


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object AnimationExample extends UI {
  verticalSync := true

  val image = Image("sgine.png")
  contents += image

  val time = 0.8
  val wf = image.location.x and image.location.y moveTo 200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x moveTo -200.0 and image.location.y moveTo 200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x and image.location.y moveTo -200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.location.x moveTo 200.0 and image.location.y moveTo -200.0 in time then
    image.location.x and image.location.y moveTo 0.0 in time then
    image.rotation.z moveTo 360.0 in time then
    image.rotation.z moveTo 0.0 in time pause time then
    image.color.alpha moveTo 0.0 in time then
    image.color.alpha moveTo 1.0 in time loop Int.MaxValue
  image.updates += wf
}