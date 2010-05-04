package org.sgine.space

import org.sgine.scene.query.NodeQuery
import org.sgine.scene.NodeContainer

/**
 * Contains positioned Nodes that have some bounding information and detail level information.
 * <p/>
 * Provides methods for fetching the nodes in some specific area as a SpaceView that can be moved around
 * to reveal the Nodes inside its bounds.
 * <p/>
 * Can also contain Generators that produce nodes on demand for the requested area - e.g. trees when walking through a forest.
 *
 */
trait Space extends NodeContainer {
  
  def createView(query: NodeQuery): SpaceView
}
