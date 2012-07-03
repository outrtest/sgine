package org.sgine.ui

import org.powerscala.workflow._
import org.powerscala.property.workflow._
import org.powerscala.Color
import org.powerscala.easing.Easing

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object AnimationExample extends UI with Debug {
  val image = Image("sgine.png")
  contents += image

  var count = 0
  val time = 1.0
  val wf = image.location.x and image.location.y moveTo 200.0 in time then
    invoke {
      image.color(Color.Red)
      count = 0
    } then
    image.location.x and image.location.y moveTo 0.0 in time using Easing.ElasticOut then
    invoke {
      image.color(Color.White)
    } then
    image.location.x moveTo -200.0 and image.location.y moveTo 200.0 in time then
    invoke {
      image.color(Color.Green)
    } then
    image.location.x and image.location.y moveTo 0.0 in time using Easing.BounceOut then
    invoke {
      image.color(Color.White)
    } then
    image.location.x and image.location.y moveTo -200.0 in time then
    invoke {
      image.color(Color.Blue)
    } then
    image.location.x and image.location.y moveTo 0.0 in time using Easing.QuadraticOut then
    invoke {
      image.color(Color.White)
    } then
    image.location.x moveTo 200.0 and image.location.y moveTo -200.0 in time then
    invoke {
      image.color(Color.values.random)
    } then
    image.location.x and image.location.y moveTo 0.0 in time using Easing.CircularOut then
    invoke {
      image.color(Color.White)
    } then
    image.rotation.z moveTo 360.0 in time using Easing.ElasticOut then
    image.rotation.z moveTo 0.0 in time using Easing.BounceOut pause time then
    image.color.alpha moveTo 0.0 in time then
    image.color.alpha moveTo 1.0 in time loop Int.MaxValue
  image.updates += wf
}