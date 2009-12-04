package com.sgine.scene.space


import com.sgine.scene.view.NodeView
import com.sgine.scene._

/**
 * Shows some Region of a Space at some DetailLevel.
 */
trait SpaceView extends NodeView {

  /**
   * The Space that this view is into.
   */
  def space : Space

  /**
   * The Region of the Space that this view shows.
   */
  def region : Region

  /**
   * The smallest details level visible in this view. 
   */
  def detailLevel : DetailLevel

  /**
   *   Changes the shown region.
   */
  def setRegion(newRegion: Region)

  /**
   *   Changes the shown detail level.
   */
  def setDetailLevel(detailLevel: DetailLevel)

  /**
   * True if this view shows the specified node.
   */
  def containsNode(node: Node ): Boolean

}
