package org.sgine.hierarchy

import annotation.tailrec

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Element {
  def parent: Parent

  val hierarchy = new ElementHierarchy

  protected class ElementHierarchy {
    /**
     * The element before this one or null.
     */
    def previous = parent.hierarchy.before(Element.this)

    /**
     * The element after this one or null.
     */
    def next = parent.hierarchy.after(Element.this)

    /**
     * Moves backward until it finds an Element matching the condition or null if it reaches the beginning.
     */
    def backward(condition: Element => Boolean): Element = {
      previous match {
        case null => null
        case element if (condition(element)) => element
        case element => element.hierarchy.backward(condition)
      }
    }

    /**
     * Moves forward until it finds an Element matching the condition or null if it reaches the end.
     */
    def forward(condition: Element => Boolean): Element = {
      next match {
        case null => null
        case element if (condition(element)) => element
        case element => element.hierarchy.forward(condition)
      }
    }

    /**
     * The first element hierarchically.
     */
    def first: Element = parent match {
      case null => Element.this
      case element => element.hierarchy.first
    }

    /**
     * The last element hierarchically from this level.
     */
    def last = Element.this
  }

}

object Element {
}

trait Parent extends Element {
  def children: Seq[Element]

  override val hierarchy = new ParentHierarchy

  protected class ParentHierarchy extends ElementHierarchy {
    /**
     * The element before this one hierarchically.
     */
    def before(element: Element) = findBefore(null, element, children)

    /**
     * The element after this one hierarchically.
     */
    def after(element: Element) = findAfter(null, element, children)

    override def last = if (children.nonEmpty) {
      children.last.hierarchy.last
    } else {
      super.last
    }

    @tailrec
    private def findBefore(previous: Element, element: Element, children: Seq[Element]): Element = {
      val current = children.head
      if (current == element) {
        if (previous == null) {
          Parent.this
        } else {
          previous.hierarchy.last
        }
      } else {
        findBefore(current, element, children.tail)
      }
    }

    @tailrec
    private def findAfter(previous: Element, element: Element, children: Seq[Element]): Element = {
      if (children.isEmpty) {
        null
      } else {
        val current = children.head
        if (previous == element) {
          current
        } else {
          findAfter(current, element, children.tail)
        }
      }
    }
  }

}