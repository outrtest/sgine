package com.sgine.space

/**
 * Shows some Region of a Space at some DetailLevel.
 */
trait SpaceView {

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
  def setRegion( newRegion : Region )

  /**
   *   Changes the shown detail level.
   */
  def setDetailLevel( detailLevel : DetailLevel )

 /**
   * The currently visible nodes in this view.
   */
  def nodes : Collection[Node]

  /**
   * True if this view shows the specified node.
   */
  def containsNode( node : Node ) : Boolean

  // Add / remove listeners that get notified when nodes appear or dissappear in this view.
  // TODO: Use signals library or such?
  def addNodeAppearanceListener( listener : (Node) => Unit )
  def addNodeDisappearanceListener( listener : (Node) => Unit )
  def removeNodeAppearanceListener( listener : (Node) => Unit )
  def removeNodeDisappearanceListener( listener : (Node) => Unit )

}
