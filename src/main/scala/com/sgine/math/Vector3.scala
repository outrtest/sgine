package com.sgine.math

object Vector3 {

  def apply( tuple3 : (Double,Double,Double) ) : Vector3 = new Vector3( tuple3._1, tuple3._2, tuple3._3 )
  
  /**
   * A vector with coordinates (0,0,0)
   */
  object Origo extends Vector3( 0, 0, 0 )

  /**
   * A unit vector in the direction of the x axis.  Coordinates (1,0,0)
   */
  object UnitX extends Vector3( 1, 0, 0 )

  /**
   * A unit vector in the direction of the x axis.  Coordinates (0,1,0)
   */
  object UnitY extends Vector3( 0, 1, 0 )

  /**
   * A unit vector in the direction of the x axis.  Coordinates (0,0,1)
   */
  object UnitZ extends Vector3( 0, 0, 1 )

  /**
   * A vector with coordinates (1,1,1)
   */
  // TODO: Is there some more correct name for this?
  object Ones extends Vector3( 1, 1, 1 )

}

/**
 * An immutable vector with three double values.
 * <p/>
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
   *  Calculates the length of the vector.
   */
  def length : Double = Math.sqrt( x*x + y*y + z*z )

  /**
   * Calculates the squared length of the vector.
   * <p/>
   * Faster than length, as it doesn't need to use a square root operation.
   * <p/>
   * Useful for quickly comparing the relative length of vectors.
   */
  def lengthSquared : Double = x*x + y*y + z*z


  // TODO:

  // dot product    * operator
  // cross product  X operator? (easy to confuse with coordinate x access though) or maybe 'cross'

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
