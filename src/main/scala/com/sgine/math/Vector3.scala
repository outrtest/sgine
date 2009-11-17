package com.sgine.math

object Vector3 {

  def apply( tuple3 : (Double,Double,Double) ) : Vector3 = new Vector3( tuple3._1, tuple3._2, tuple3._3 )
  

}

/**
 * An immutable vector with three double values.
 * Calculation operations will return a result Vector3 instead of modifying this one.
 */
case class Vector3( x : Double = 0.0, y : Double = 0.0, z : Double = 0.0 ) {

  def + ( other : Vector3 ) : Vector3 = new Vector3( x + other.x, y + other.y, z + other.z )
  def - ( other : Vector3 ) : Vector3 = new Vector3( x - other.x, y - other.y, z - other.z )
  def * ( scalar : Double ) : Vector3 = new Vector3( x * scalar, y * scalar, z * scalar )
  def / ( scalar : Double ) : Vector3 = new Vector3( x / scalar, y / scalar, z / scalar )

  def + ( ox : Double, oy : Double, oz : Double ) : Vector3 = new Vector3( x + ox, y + oy, z + oz )
  def - ( ox : Double, oy : Double, oz : Double ) : Vector3 = new Vector3( x - ox, y - oy, z - oz )


  /**
   * Calculates the length of the vector.
   */
  def length : Double = Math.sqrt( x*x + y*y + z*z )

  /**
   * Calculates the squared length of the vector.
   * Faster than length, as it doesn't need to use a square root operation.
   * Useful for quickly comparing the relative length of vectors.
   */
  def lengthSquared : Double = x*x + y*y + z*z


  // TODO:

  // dot product    * operator
  // cross product  x operator? or maybe >< or 'cross'

  // distance
  // squaredDistance

  // normalize

  // unary minus


  /**
   * Returns a three element list with the x, y, and z coordinates of the Vector3 in that order.
   */
  def toList: List[Double] = List(x, y, z)

  /**
   * Returns a tuple with the x, y, and z coordinates of the Vector3 in that order.
   */
  def toTuple : (Double, Double, Double) = (x, y, z)

  override def toString = "Vector3("+x+", "+y+", "+z+")"

}
