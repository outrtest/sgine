package org.sgine

/**
 * Parent represents an object that has children.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Parent[C] {
  def children: Seq[C]

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
  def descendant[T](matcher: T => Boolean, maxDepth: Int = Int.MaxValue, children: Seq[C] = this.children)(implicit manifest: Manifest[T]): Option[T] = {
    if (children.nonEmpty) {
      // if (manifest.erasure.isAssignableFrom(p.getClass) && matcher(p.asInstanceOf[T])) {
      val child = children.head
      if (manifest.erasure.isAssignableFrom(child.asInstanceOf[AnyRef].getClass) && matcher(child.asInstanceOf[T])) {
        Option(child.asInstanceOf[T])
      } else {
        val result = descendant(matcher, maxDepth, children.tail)(manifest)
        if (result == None && maxDepth > 1) {
          child match {
            case parent: Parent[_] => parent.descendant(matcher, maxDepth - 1, parent.children)
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
}