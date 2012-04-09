package org.sgine.ui.easing

import org.sgine.{Enumerated, EnumEntry}

sealed class Easing(val f: Function4[Double, Double, Double, Double, Double]) extends EnumEntry[Easing]

object Easing extends Enumerated[Easing] {
  val BackIn = new Easing(Back.easeIn)
  val BackOut = new Easing(Back.easeOut)
  val BackInOut = new Easing(Back.easeInOut)
  val BounceIn = new Easing(Bounce.easeIn)
  val BounceOut = new Easing(Bounce.easeOut)
  val BounceInOut = new Easing(Bounce.easeInOut)
  val CircularIn = new Easing(Circular.easeIn)
  val CircularOut = new Easing(Circular.easeOut)
  val CircularInOut = new Easing(Circular.easeInOut)
  val CubicIn = new Easing(Cubic.easeIn)
  val CubicOut = new Easing(Cubic.easeOut)
  val CubicInOut = new Easing(Cubic.easeInOut)
  val ElasticIn = new Easing(Elastic.easeIn)
  val ElasticOut = new Easing(Elastic.easeOut)
  val ElasticInOut = new Easing(Elastic.easeInOut)
  val ExponentialIn = new Easing(Exponential.easeIn)
  val ExponentialOut = new Easing(Exponential.easeOut)
  val ExponentialInOut = new Easing(Exponential.easeInOut)
  val LinearIn = new Easing(Linear.easeIn)
  val LinearOut = new Easing(Linear.easeOut)
  val LinearInOut = new Easing(Linear.easeInOut)
  val QuadraticIn = new Easing(Quadratic.easeIn)
  val QuadraticOut = new Easing(Quadratic.easeOut)
  val QuadraticInOut = new Easing(Quadratic.easeInOut)
  val QuarticIn = new Easing(Quartic.easeIn)
  val QuarticOut = new Easing(Quartic.easeOut)
  val QuarticInOut = new Easing(Quartic.easeInOut)
  val QuinticIn = new Easing(Quintic.easeIn)
  val QuinticOut = new Easing(Quintic.easeOut)
  val QuinticInOut = new Easing(Quintic.easeInOut)
  val SineIn = new Easing(Sine.easeIn)
  val SineOut = new Easing(Sine.easeOut)
  val SineInOut = new Easing(Sine.easeInOut)
}