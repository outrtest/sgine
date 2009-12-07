package org.sgine.raster

/**
 * A numerical function that calculates some output values for a given set of input values.
 *
 * Can be used to fill in a raster with the values for some area.
 */
trait Function {

  /**
   * The identifiers and descriptions of the parameters taken by this function.
   */
  def parameters: List[ParameterInfo]

  /**
   * The identifiers and descriptions of the results produced by this function.
   */
  def results: List[ParameterInfo]

  /**
   * The value of the field with the specified input values.
   *
   * The parameters should be in the order indicated by the parameters method, and the results will
   * be in the order indicated by the results method.
   */
  def apply(parameters: Float*): Array[Float]

  /**
   * The value of the field with the specified named input values.
   * Returns the named output values.
   */
  def apply(parameters: Map[Symbol, Float]): Map[Symbol, Float]

  /**
   * Fills the specified raster with the output of this function by interpolating the given corner parameters
   * over the area of the raster.
   *
   * The raster should contain an equal number of channels as there are in the result of this function.
   */
  def fillRaster(raster: Raster, cornerParameters: CornerParameters)

}
