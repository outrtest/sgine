package org.sgine.hierarchy

import annotation.tailrec

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Named {
  def name: String

  def hierarchicalNames = Named.buildHierarchicalName(this)
  def hierarchicalName = hierarchicalNames.mkString(".")
}

object Named {
  @tailrec
  def buildHierarchicalName(current: Any, tail: List[String] = Nil): List[String] = current match {
    case child: Child with Named => buildHierarchicalName(child.parent, if (child.name != null) child.name :: tail else tail)
    case child: Child => buildHierarchicalName(child.parent, tail)
    case named: Named => if (named.name != null) named.name :: tail else tail
    case _ => tail
  }
}
