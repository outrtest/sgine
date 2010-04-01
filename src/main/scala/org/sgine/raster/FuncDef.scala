package org.sgine.raster


/**
 * A function generator that can create a function with some constant parameters.
 *
 * Can contain constant as well as input and output parameters.
 */
// TODO: Implement detection of Input and Output values on compilation, and add them.
trait FuncDef {

  /**
   * Generates the java source for this function definition.
   */
  //def generateSouce() : String

  /**
   * Creates a function with the specified constant parameters.
   */
  //def createFunction(): Func

}
