package org.sgine.raster

/**
 * Four sets of named parameter values, used when filling in a raster with the values from a function.
 */
case class CornerParameters(
        topLeft: Map[Symbol, Float],
        topRight: Map[Symbol, Float],
        bottomLeft: Map[Symbol, Float],
        bottomRight: Map[Symbol, Float])
