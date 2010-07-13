package org.sgine.easing

/**
 * Easing related utilities.
 */
object Easing {
  /**
   * The type of the easing functions.
   */
  type EasingFunction = Function4[Double, Double, Double, Double, Double]
  
  val values = List(Back.easeIn _, Back.easeOut _, Back.easeInOut _,
		   			Bounce.easeIn _, Bounce.easeOut _, Bounce.easeInOut _,
		   			Circular.easeIn _, Circular.easeOut _, Circular.easeInOut _,
		   			Cubic.easeIn _, Cubic.easeOut _, Cubic.easeInOut _,
		   			Elastic.easeIn _, Elastic.easeOut _, Elastic.easeInOut _,
		   			Exponential.easeIn _, Exponential.easeOut _, Exponential.easeInOut _,
		   			Linear.easeIn _, Linear.easeOut _, Linear.easeInOut _,
		   			Quadratic.easeIn _, Quadratic.easeOut _, Quadratic.easeInOut _,
		   			Quartic.easeIn _, Quartic.easeOut _, Quartic.easeInOut _,
		   			Quintic.easeIn _, Quintic.easeOut _, Quintic.easeInOut _,
		   			Sine.easeIn _, Sine.easeOut _, Sine.easeInOut _)
  def random = values(new scala.util.Random().nextInt(values.length))
}