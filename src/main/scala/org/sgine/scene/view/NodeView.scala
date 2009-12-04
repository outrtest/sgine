package com.sgine.scene.view

import com.sgine.util.Signal
import com.sgine.scene.Node

/**
 * A view to some Nodes that match a query in a NodeContainer.
 */
// (Temporary implementation notes):
// A view implementation should listen to added and removed nodes in the collection that it monitors.
// In addition, it needs to listen to any changes to the nodes that are relevant for its query.
//
// A simple implementaion could assume the nodes are just tested for equality.
//
// Later we can implement more complex queries that mtach against some Property values of the node
//  - in these cases, the view would have to listen to all property changes that are relevant for it in all the nodes in the collection it monitors.
// This can be somewhat heavy..  We're drifting towards a database like implementation there.
// Maybe it could be optimized with some hints, or efficient datastructures.  Depends on the cases.
//
// We'll also need spatial views for monitoring all nodes inside some area.  For this, we can use spatial
// query datatypes like quadtrees or r-trees, and should manage ok.
trait NodeView extends Iterable[Node] {

  /**
   * The nodes currently in this view.
   */
  def nodes: Iterable[Node]

  /**
   * Implementation of the Iterable trait.
   */
  def iterator = nodes.iterator

  /**
   * A signal that gets triggered when a Node appears in the view.
   * You can register a listener with this signal to be notified about appearing nodes.
   */
  def nodeAppearance: Signal[Node]

  /**
   * A signal that gets triggered when a Node disappears from the view.
   * You can register a listener with this signal to be notified about disappearing nodes.
   */
  def nodeDisappearance: Signal[Node]

  /**
   * Destroys this view, that is, stops listening to changes and empties the node collection.
   */
  def destroy()
}
