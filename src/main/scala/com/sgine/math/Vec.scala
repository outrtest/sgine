package com.sgine.math

object Vec {

  def apply( tuple3 : (Double,Double,Double) ) : Vec = new Vec( tuple3._1, tuple3._2, tuple3._3 )
  

}

/**
 * A vector with three double values.
 */
case class Vec( x : Double = 0.0, y : Double = 0.0, z : Double = 0.0 ) {

  def + ( other : Vec ) : Vec = new Vec( x + other.x, y + other.y, z + other.z )
  def - ( other : Vec ) : Vec = new Vec( x - other.x, y - other.y, z - other.z )
  def * ( scalar : Double ) : Vec = new Vec( x * scalar, y * scalar, z * scalar )
  def / ( scalar : Double ) : Vec = new Vec( x / scalar, y / scalar, z / scalar )

  def + ( ox : Double, oy : Double, oz : Double ) : Vec = new Vec( x + ox, y + oy, z + oz )
  def - ( ox : Double, oy : Double, oz : Double ) : Vec = new Vec( x - ox, y - oy, z - oz )


  // TODO:

  // dot product    * operator
  // cross product  x operator? or maybe >< or 'cross'

  // length
  // squaredLength

  // distance
  // squaredDistance

  // normalize

  // unary minus

  // toString
  // toTuple
  // toList?

  
}
