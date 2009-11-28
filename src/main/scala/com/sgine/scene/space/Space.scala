package com.sgine.scene.space

/**
 * Contains positioned Nodes that have some bounding information and detail level information.
 * <p/>
 * Provides methods for fetching the nodes in some specific area as a SpaceView that can be moved around
 * to reveal the Nodes inside its bounds.
 * <p/>
 * Can also contain Generators that produce nodes on demand for the requested area - e.g. trees when walking through a forest.
 *
 */
trait Space {

  /**
   * Creates a new view, initially showing the specified Region at the specified DetailLevel.
   */
  def createView( region : Region, detailLevel : DetailLevel ) : SpaceView

  /**
   * Disables an existing view to this Space.  The view will no longer be updated, and will be in an undefined state.
   */
  def disableView( view : SpaceView )

}
