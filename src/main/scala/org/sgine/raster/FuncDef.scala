package org.sgine.raster

/**
 * A function generator that can create a function with some constant parameters.
 */
trait FuncDef {

  /**
   * The identifiers and descriptions of the accepted constant parameters.
   */
  def constants: List[ParameterInfo]

  /**
   * The identifiers and descriptions of the accepted input values.
   */
  def inputs: List[ParameterInfo]

  /**
   * The identifiers and descriptions of the provided output results.
   */
  def outputs: List[ParameterInfo]

  def update(input: Symbol, source: FuncDef)
  def apply(output: Symbol)

  /**
   * Creates a function with the specified constant parameters.
   */
  def createFunction(parameters: Map[Symbol, Float]): Func

}
