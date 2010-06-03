package org.sgine.raster.example

import org.sgine.raster.ArrayImageGenerator
import org.sgine.raster.functions.{Const, Sin}

/**
 * Example on generating rasters.
 */
object RasterCalculationExample {
  def main(args: Array[String]) {

    val imageGenerator = new ArrayImageGenerator()
    val sin = new Sin()

    sin.in <-- imageGenerator.x

    imageGenerator.red <-- sin.out
    imageGenerator.green <-- Const(0.5f).out
    imageGenerator.blue <-- imageGenerator.y


    // TODO: Compile image genertor and create a picture with it
  }
}
