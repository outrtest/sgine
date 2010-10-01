package org.sgine.easing

import org.sgine.core.Enum
import org.sgine.core.Enumerated

sealed class Easing(val f: Function4[Double, Double, Double, Double, Double]) extends Enum

object Easing extends Enumerated[Easing] {
	type EasingFunction = Function4[Double, Double, Double, Double, Double]
	
	case object BackIn extends Easing(Back.easeIn)
	case object BackOut extends Easing(Back.easeOut)
	case object BackInOut extends Easing(Back.easeInOut)
	case object BounceIn extends Easing(Bounce.easeIn)
	case object BounceOut extends Easing(Bounce.easeOut)
	case object BounceInOut extends Easing(Bounce.easeInOut)
	case object CircularIn extends Easing(Circular.easeIn)
	case object CircularOut extends Easing(Circular.easeOut)
	case object CircularInOut extends Easing(Circular.easeInOut)
	case object CubicIn extends Easing(Cubic.easeIn)
	case object CubicOut extends Easing(Cubic.easeOut)
	case object CubicInOut extends Easing(Cubic.easeInOut)
	case object ElasticIn extends Easing(Elastic.easeIn)
	case object ElasticOut extends Easing(Elastic.easeOut)
	case object ElasticInOut extends Easing(Elastic.easeInOut)
	case object ExponentialIn extends Easing(Exponential.easeIn)
	case object ExponentialOut extends Easing(Exponential.easeOut)
	case object ExponentialInOut extends Easing(Exponential.easeInOut)
	case object LinearIn extends Easing(Linear.easeIn)
	case object LinearOut extends Easing(Linear.easeOut)
	case object LinearInOut extends Easing(Linear.easeInOut)
	case object QuadraticIn extends Easing(Quadratic.easeIn)
	case object QuadraticOut extends Easing(Quadratic.easeOut)
	case object QuadraticInOut extends Easing(Quadratic.easeInOut)
	case object QuarticIn extends Easing(Quartic.easeIn)
	case object QuarticOut extends Easing(Quartic.easeOut)
	case object QuarticInOut extends Easing(Quartic.easeInOut)
	case object QuinticIn extends Easing(Quintic.easeIn)
	case object QuinticOut extends Easing(Quintic.easeOut)
	case object QuinticInOut extends Easing(Quintic.easeInOut)
	case object SineIn extends Easing(Sine.easeIn)
	case object SineOut extends Easing(Sine.easeOut)
	case object SineInOut extends Easing(Sine.easeInOut)
	
	Easing(BackIn, BackOut, BackInOut,
		   BounceIn, BounceOut, BounceInOut,
		   CircularIn, CircularOut, CircularInOut,
		   CubicIn, CubicOut, CubicInOut,
		   ElasticIn, ElasticOut, ElasticInOut,
		   ExponentialIn, ExponentialOut, ExponentialInOut,
		   LinearIn, LinearOut, LinearInOut,
		   QuadraticIn, QuadraticOut, QuadraticInOut,
		   QuarticIn, QuarticOut, QuarticInOut,
		   QuinticIn, QuinticOut, QuinticInOut,
		   SineIn, SineOut, SineInOut)
}