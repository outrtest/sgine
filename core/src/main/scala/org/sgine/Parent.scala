package org.sgine

/**
 * Parent represents an object that has children.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Parent[C] {
  def children: List[C]

  /**
   * Returns true if the value passed is in the ancestry hierarchy for this Child.
   */
  def hasDescendant[T](value: T)(implicit manifest: Manifest[T]) = descendant((t: T) => t == value)(manifest) != None

  /**
   * Uses the supplied matching function to return the first descendant match given the specified type or None if no
   * match is found.
   */
  def descendant[T](matcher: T => Boolean, children: List[C] = this.children)(implicit manifest: Manifest[T]): Option[T] = {
    if (children.nonEmpty) {
      // if (manifest.erasure.isAssignableFrom(p.getClass) && matcher(p.asInstanceOf[T])) {
      val child = children.head
      if (manifest.erasure.isAssignableFrom(child.asInstanceOf[AnyRef].getClass) && matcher(child.asInstanceOf[T])) {
        Option(child.asInstanceOf[T])
      } else {
        val result = descendant(matcher, children.tail)(manifest)
        if (result == None) {
          child match {
            case parent: Parent[_] => parent.descendant(matcher)
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