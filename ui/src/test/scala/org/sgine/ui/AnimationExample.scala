package org.sgine.ui

import org.sgine.workflow._
import org.sgine.property.workflow._
import org.sgine.Color

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object AnimationExample extends UI {
  verticalSync := true
  fixedTimestep := 1.0 / 60.0

  val image = Image("sgine.png")
  contents += image

  var count = 0
  val time = 0.8
  val wf = image.location.x and image.location.y moveTo 200.0 in time then
    invoke {
      image.color(Color.Red)
      count = 0
    } then
    waitFor {
      println("Count: %s".format(count))
      count += 1
      count >= 10
    } then
    image.location.x and image.location.y moveTo 0.0 in time then
    invoke {
      image.color(Color.White)
    } then
    image.location.x moveTo -200.0 and image.location.y moveTo 200.0 in time then
    invoke {
      image.color(Color.Green)
    } then
    image.location.x and image.location.y moveTo 0.0 in time then
    invoke {
      image.color(Color.White)
    } then
    image.location.x and image.location.y moveTo -200.0 in time then
    invoke {
      image.color(Color.Blue)
    } then
    image.location.x and image.location.y moveTo 0.0 in time then
    invoke {
      image.color(Color.White)
    } then
    image.location.x moveTo 200.0 and image.location.y moveTo -200.0 in time then
    invoke {
      image.color(Color.values.random)
    } then
    image.location.x and image.location.y moveTo 0.0 in time then
    invoke {
      image.color(Color.White)
    } then
    image.rotation.z moveTo 360.0 in time then
    image.rotation.z moveTo 0.0 in time pause time then
    image.color.alpha moveTo 0.0 in time then
    image.color.alpha moveTo 1.0 in time loop Int.MaxValue
  image.updates += wf
}