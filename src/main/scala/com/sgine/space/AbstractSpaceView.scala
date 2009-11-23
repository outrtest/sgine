package com.sgine.space

import collection.JavaConversions._
import java.util.{HashSet, ArrayList}

/**
 * Provides some default implementations for some SpaceView methods.
 */
// TODO: Should this just be in SpaceView directly instead?
trait AbstractSpaceView extends SpaceView {

  private var _region : Region = null
  private var _detail : DetailLevel = null
  private var appearanceListeners = new ArrayList[(Node) => Unit]
  private var disappearanceListeners = new ArrayList[(Node) => Unit]
  private var currentNodes = new HashSet[Node]

  final def addNodeAppearanceListener(listener: (Node) => Unit) = appearanceListeners.add( listener )
  final def addNodeDisappearanceListener(listener: (Node) => Unit) = disappearanceListeners.add( listener )
  final def removeNodeAppearanceListener(listener: (Node) => Unit) = appearanceListeners.remove( listener )
  final def removeNodeDisappearanceListener(listener: (Node) => Unit) = disappearanceListeners.remove( listener )

  final def detailLevel = _detail
  final def region = _region


  final def nodes = currentNodes

  final def containsNode(node: Node) = currentNodes.contains( node )

  final def setRegion(newRegion: Region) {
    val oldRegion = _region
    _region = newRegion
    onRegionChanged( oldRegion, newRegion )
  }

  final def setDetailLevel(newDetail: DetailLevel) {
    val oldDetail = _detail
    _detail = newDetail
    onDetailLevelChanged(oldDetail, newDetail)
  }

  /**
   * Called by the view implementation if a node appears
   */
  final def notifyNodeAppeared( node : Node ){
    if (!currentNodes.contains( node )) {
      currentNodes.add( node )
      appearanceListeners foreach{ _(node) }
    }
  }

  /**
   * Called by the view implementation if a node disappears
   */
  final def notifyNodeDisappeared( node : Node ) {
    if (currentNodes.contains( node )) {
      currentNodes.remove( node )
      disappearanceListeners foreach{ _(node) }
    }
  }

  protected def onRegionChanged( oldRegion : Region, newRegion : Region )
  protected def onDetailLevelChanged( oldDetail : DetailLevel, newDetail : DetailLevel )

}
