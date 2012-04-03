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

    /**
     * Returns true if the value passed is in the ancestry hierarchy for this Child.
     */
    def hasAncestor[T](value: T, maxDepth: Int = Int.MaxValue)(implicit manifest: Manifest[T]) = ancestor((t: T) => t == value, maxDepth)(manifest) != None

    /**
     * Returns true if the value passed is the parent of this Child.
     */
    def hasParent[T](value: T)(implicit manifest: Manifest[T]) = hasAncestor(value, 1)(manifest)

    /**
     * Uses the supplied matching function to return the first ancestor match given the specified type or None if no
     * match is found.
     */
    def ancestor[T](matcher: T => Boolean, maxDepth: Int = Int.MaxValue)(implicit manifest: Manifest[T]): Option[T] = {
      val p = parent
      if (p != null) {
        if (manifest.erasure.isAssignableFrom(p.asInstanceOf[AnyRef].getClass) && matcher(p.asInstanceOf[T])) {
          Option(p.asInstanceOf[T])
        } else if (maxDepth > 1) {
          p.hierarchy.ancestor[T](matcher, maxDepth - 1)(manifest)
        } else {
          None
        }
      } else {
        None
      }
    }

    /**
     * Processes up the ancestry tree through parents to find the first matching ancestor of the
     * generic type T and invokes the supplied function on it.
     */
    def ancestor[T](f: T => Unit)(implicit manifest: Manifest[T]): Unit = {
      val p = parent
      if (p != null) {
        if (manifest.erasure.isAssignableFrom(p.getClass)) {
          f(p.asInstanceOf[T])
        } else {
          p.hierarchy.ancestor(f)(manifest)
        }
      }
    }

    /**
     * Processes up the ancestry tree through parents executing the supplied function on all parents
     * that match the supplied generic type.
     */
    def ancestors[T](f: T => Unit)(implicit manifest: Manifest[T]): Unit = {
      val p = parent
      if (p != null) {
        if (manifest.erasure.isAssignableFrom(p.getClass)) {
          f(p.asInstanceOf[T])
        }
        p.hierarchy.ancestor(f)(manifest)
      }
    }

    /**
     * Invokes the supplied method if this class matches the supplied generic type.
     *
     * Returns true if the generic type matched.
     */
    def apply[T](f: T => Unit)(implicit manifest: Manifest[T]): Boolean = {
      if (manifest.erasure.isAssignableFrom(getClass)) {
        f(this.asInstanceOf[T])
        true
      }
      else {
        false
      }
    }
  }

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

    /**
     * Returns true if the value passed is in the descendant hierarchy for this Parent.
     */
    def hasDescendant[T](value: T, maxDepth: Int = Int.MaxValue)(implicit manifest: Manifest[T]) = descendant((t: T) => t == value, maxDepth)(manifest) != None

    /**
     * Returns true if the value passed is a child of this Parent.
     */
    def hasChild[T](value: T)(implicit manifest: Manifest[T]) = hasDescendant(value, 1)(manifest)

    /**
     * Uses the supplied matching function to return the first descendant match given the specified type or None if no
     * match is found.
     */
    def descendant[T](matcher: T => Boolean, maxDepth: Int = Int.MaxValue, children: Seq[Element] = Parent.this.children)(implicit manifest: Manifest[T]): Option[T] = {
      if (children.nonEmpty) {
        // if (manifest.erasure.isAssignableFrom(p.getClass) && matcher(p.asInstanceOf[T])) {
        val child = children.head
        if (manifest.erasure.isAssignableFrom(child.asInstanceOf[AnyRef].getClass) && matcher(child.asInstanceOf[T])) {
          Option(child.asInstanceOf[T])
        } else {
          val result = descendant(matcher, maxDepth, children.tail)(manifest)
          if (result == None && maxDepth > 1) {
            child match {
              case parent: Parent => parent.hierarchy.descendant(matcher, maxDepth - 1, parent.children)
              case _ => None
            }
          } else {
            result
          }
        }
      } else {
        None
      }
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