package com.sgine.math

object Fixed {

  val ShiftAmount = 16

  val decimalDivisor = 1 << ShiftAmount
  val factorD = 1.0 * decimalDivisor
  val factorF = 1.0f * decimalDivisor
  val invFactorD = 1.0 / decimalDivisor
  val invFactorF = 1.0f / decimalDivisor

  val One  = Fixed( 1 << ShiftAmount )
  val Zero = Fixed( 0 )

  // TODO: Use apply instead, and create a constructor that takes boolean flag for constructing directly with raw value.
  def make( v : Int ) = Fixed( v << ShiftAmount )
  def make( v : Long ) = Fixed( v << ShiftAmount )
  def make( v : Float ) = Fixed( (v * factorF).toLong )
  def make( v : Double ) = Fixed( (v * factorD).toLong  )

}

/**
 * Encapsulates a 48.16 fixed point number.
 *
 * If used as coordinates where 1 unit = 1m, it gives about 0.03 light years range at 0.01 mm resolution.
 */
final case class Fixed( rawValue : Long ) extends Ordered[Fixed] {

  def toFloat = rawValue * Fixed.invFactorF
  def toDouble = rawValue * Fixed.invFactorD

  def + ( f : Fixed ) = Fixed( rawValue + f.rawValue )
  def - ( f : Fixed ) = Fixed( rawValue - f.rawValue )
  def * ( f : Fixed ) = Fixed( (rawValue * f.rawValue) >> Fixed.ShiftAmount )
  def / ( f : Fixed ) = Fixed( (rawValue << Fixed.ShiftAmount) / f.rawValue )

  def compare(that: Fixed) = {
    if (rawValue < that.rawValue) -1
    else if (rawValue > that.rawValue) 1
    else 0
  }
}
