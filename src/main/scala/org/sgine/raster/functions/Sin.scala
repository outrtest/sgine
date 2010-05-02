package org.sgine.raster.functions

import org.sgine.raster.{Input, Output, FuncDef}

class Sin extends FuncDef {
  val in: Input = new Input(this, 'in)
  val out: Output = new Output(this, 'out)
}