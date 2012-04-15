package org.sgine.hierarchy

import annotation.tailrec

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Named {
  def name: String

  def hierarchicalName = Named.buildHierarchicalName(this)
}

object Named {
  @tailrec
  def buildHierarchicalName(current: Any, tail: List[String] = Nil): List[String] = current match {
    case child: Child with Named => buildHierarchicalName(child.parent, child.name :: tail)
    case child: Child => buildHierarchicalName(child.parent, tail)
    case named: Named => named.name :: tail
    case _ => tail
  }
}
