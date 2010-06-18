package org.sgine.raster

/**
 * Compiles functions that generate an array of packed RGBA color codes.
 *
 * For each pixel, sets the x and y values, and then runs the functions to calculate red, green, blue, and alpha.
 */
class ArrayImageGenerator extends FuncDef {
  val x     = Output(this, 'x)
  val y     = Output(this, 'y)
  val red   = Input(this, 'red)
  val green = Input(this, 'green)
  val blue  = Input(this, 'blue)
  val alpha = Input(this, 'alpha)
}
