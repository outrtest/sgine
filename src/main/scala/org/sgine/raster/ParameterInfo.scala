package org.sgine.raster

/**
 * Information on a parameter or return value used by a Func, or a channel used by a Raster.
 *
 * Has an unique identifier (within the function/raster), and a user readable description.
 */
case class ParameterInfo(identifier: Symbol, description: String) {
  override def toString = identifier.name + ": " + description
}
