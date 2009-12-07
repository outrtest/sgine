package org.sgine.raster

/**
 * A function generator that can create a function with some constant parameters.
 */
trait ParametrizableFunction {

  /**
   * The identifiers and descriptions of the accepted constant parameters.
   */
  def parameters: List[ParameterInfo]

  /**
   * Creates a function with the specified constant parameters.
   */
  def createFunction(parameters: Map[Symbol, Float]): Function

}
