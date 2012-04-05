package org.sgine.hierarchy


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Element {
  private var _parent: Parent = _
  def parent = _parent

  val hierarchy = new ElementHierarchy

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
    def backward[E <: Element](condition: E => Boolean = (e: E) => true)(implicit manifest: Manifest[E]): E = {
      previous match {
        case null => null.asInstanceOf[E]
        case element if (matchesManifest(element, manifest) && condition(element.asInstanceOf[E])) => {
          element.asInstanceOf[E]
        }
        case element => element.hierarchy.backward[E](condition)(manifest)
      }
    }

    /**
     * Moves forward until it finds an Element matching the condition or null if it reaches the end.
     */
    def forward[E <: Element](condition: E => Boolean = (e: E) => true)(implicit manifest: Manifest[E]): E = {
      next match {
        case null => null.asInstanceOf[E]
        case element if (matchesManifest(element, manifest) && condition(element.asInstanceOf[E])) => {
          element.asInstanceOf[E]
        }
        case element => element.hierarchy.forward[E](condition)(manifest)
      }
    }

    /**
     * The first element hierarchically.
     */
    def first: Element = parent match {
      case null => Element.this
      case element => element.hierarchy.first
    }

    protected def matchesManifest[T <: AnyRef](value: AnyRef, manifest: Manifest[T]) = manifest.erasure.isAssignableFrom(value.getClass)

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

//    /**
//     * Processes up the ancestry tree through parents to find the first matching ancestor of the
//     * generic type T and invokes the supplied function on it.
//     */
//    def ancestor[T](f: T => Unit)(implicit manifest: Manifest[T]): Unit = {
//      val p = parent
//      if (p != null) {
//        if (manifest.erasure.isAssignableFrom(p.getClass)) {
//          f(p.asInstanceOf[T])
//        } else {
//          p.hierarchy.ancestor(f)(manifest)
//        }
//      }
//    }

    /**
     * Processes up the ancestry tree through parents executing the supplied function on all parents
     * that match the supplied generic type.
     */
//    def ancestors[T](f: T => Unit)(implicit manifest: Manifest[T]): Unit = {
//      val p = parent
//      if (p != null) {
//        if (manifest.erasure.isAssignableFrom(p.getClass)) {
//          f(p.asInstanceOf[T])
//        }
//        p.hierarchy.ancestor[T](f)(manifest)
//      }
//    }
  }
}

object Element {
  def assignParent(element: Element, parent: Parent) = element._parent = parent
}