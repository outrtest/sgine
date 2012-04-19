package org.sgine.hierarchy

import annotation.tailrec

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Named {
  def name: String

  def hierarchicalNames(from: Any = null) = Named.buildHierarchicalName(this, from)
  def hierarchicalName(from: Any = null) = hierarchicalNames(from).mkString(".")
  def hierarchicalMatch(names: String) = hierarchicalMatchCheck(names.split("[.]").toList.reverse)

  @tailrec
  private def hierarchicalMatchCheck(names: List[String]): Boolean = {
    if (names.nonEmpty) {
      val n = names.head
      if (n == name || n == getClass.getSimpleName) {
        names.tail match {
          case Nil => true
          case others => this match {
            case child: Child => child.parent match {
              case named: Named => named.hierarchicalMatchCheck(others)
              case _ => false
            }
            case _ => false
          }
        }
      } else {
        false
      }
    } else {
      false
    }
  }
}

object Named {
  @tailrec
  def buildHierarchicalName(current: Any, from: Any, tail: List[String] = Nil): List[String] = current match {
    case _ if (current == from) => tail
    case child: Child with Named => buildHierarchicalName(child.parent, from, if (child.name != null) child.name :: tail else tail)
    case child: Child => buildHierarchicalName(child.parent, from, tail)
    case named: Named => if (named.name != null) named.name :: tail else tail
    case _ => tail
  }
}
