package org.sgine.raster.functions

import org.sgine.raster.{Output, FuncDef}

/**
 * A constant value.
 */
case class Const(value : Float) extends FuncDef {
  val out = new Output(this, 'out)
}
