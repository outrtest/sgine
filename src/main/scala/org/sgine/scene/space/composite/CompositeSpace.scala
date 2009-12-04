package org.sgine.scene.space.composite

import org.sgine.scene._
import org.sgine.scene.space._
import query.NodeQuery

/**
 * A Space that combines the content of several other Spaces, and presents them all together as one Space.
 * <p/>
 * Useful e.g. for combining content produced from procedural generator spaces,
 * dynamic content spaces, and static content spaces.
 */
class CompositeSpace extends Space {

  // TODO: Add / remove member Spaces.

  // TODO: Implement
  def iterator = null
  def createView(query: NodeQuery) = null

}
