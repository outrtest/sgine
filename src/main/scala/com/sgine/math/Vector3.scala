package com.sgine.math

object Vector3 {

  def apply( tuple3 : (Double,Double,Double) ) : Vector3 = new Vector3( tuple3._1, tuple3._2, tuple3._3 )
  

}

/**
 * A vector with three double values.
 */
case class Vector3( x : Double = 0.0, y : Double = 0.0, z : Double = 0.0 ) {

  def + ( other : Vector3 ) : Vector3 = new Vector3( x + other.x, y + other.y, z + other.z )
  def - ( other : Vector3 ) : Vector3 = new Vector3( x - other.x, y - other.y, z - other.z )
  def * ( scalar : Double ) : Vector3 = new Vector3( x * scalar, y * scalar, z * scalar )
  def / ( scalar : Double ) : Vector3 = new Vector3( x / scalar, y / scalar, z / scalar )

  def + ( ox : Double, oy : Double, oz : Double ) : Vector3 = new Vector3( x + ox, y + oy, z + oz )
  def - ( ox : Double, oy : Double, oz : Double ) : Vector3 = new Vector3( x - ox, y - oy, z - oz )


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
