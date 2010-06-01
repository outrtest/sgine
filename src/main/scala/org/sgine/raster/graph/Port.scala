package org.sgine.raster.graph

/**
 * A socket or plug in a component that can be connected with another Port in some other (or the same) component.
 */
// TODO: Maybe ports could just be implemented as properties?
case class Port(identifier: Symbol, description: String)
