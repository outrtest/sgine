package com.sgine.scene

/**
 * A simple boolean condition version of a NodeQuery.
 */
final case class ConditionNodeQuery(condition: Node => Boolean) extends NodeQuery {

  def matches(node: Node): Boolean = condition(node)

}
