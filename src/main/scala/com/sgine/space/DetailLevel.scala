package com.sgine.space

/**
 * Represents some level of detail.
 */
final case class DetailLevel( value : Double ) extends Ordered[DetailLevel] {

  def compare(other: DetailLevel) = value.compare( other.value )

}
