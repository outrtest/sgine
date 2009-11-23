package com.sgine.space

import collection.JavaConversions._

import java.util.ArrayList

/**
 * A simple ArrayList backed brute-force implementation of Space.
 */
class SimpleSpace extends EditableSpace {

  private val nodes = new ArrayList[Node]
  private val views = new ArrayList[SimpleSpaceView]

  class SimpleSpaceView extends AbstractSpaceView {

    def space = SimpleSpace.this

    protected def onDetailLevelChanged(oldDetail: DetailLevel, newDetail: DetailLevel) {
      updateView( SimpleSpaceView.this )
    }

    protected def onRegionChanged(oldRegion: Region, newRegion: Region) {
      updateView( SimpleSpaceView.this )
    }
  }


  private def updateView( view : SimpleSpaceView ) {
    nodes foreach { node =>
      checkDisappearance( view, node )
      checkAppearance( view, node )
    }

  }

  private def checkAppearance( view : SimpleSpaceView, node : Node ) {
    // TODO: Implement DetailLevel check
    if( !view.containsNode(node) &&
        view.region.intersects( node ) )
      view.notifyNodeAppeared( node )
  }

  private def checkDisappearance( view : SimpleSpaceView, node : Node ) {
    // TODO: Implement DetailLevel check
    if( view.containsNode(node) &&
        !view.region.intersects( node ) )
      view.notifyNodeDisappeared( node )
  }

  def removeNode(node: Node) {
    nodes.remove( node )

    views foreach { view =>
      view.notifyNodeDisappeared( node )
    }
  }

  def addNode(node: Node) {
    nodes.add( node )

    views foreach { view =>
      checkAppearance(view, node)
    }
  }

  def disableView(view: SpaceView) {
    views.remove( view )
  }

  def createView(region: Region, detailLevel: DetailLevel) : SpaceView =  {
    val view = new SimpleSpaceView()
    views.add( view )
    updateView( view )
    view
  }
}
