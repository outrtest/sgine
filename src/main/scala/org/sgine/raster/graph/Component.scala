package org.sgine.raster.graph

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Components can be connected together through their ports into a graph.
 *
 * Ports can be defined either in subclasses as val:s or similar on a class, or completely dynamically.
 */
// TODO: Might be made to implement Node, or that can be done at when used.
class Component {

  private val ports = new ConcurrentLinkedQueue[Port]()


}