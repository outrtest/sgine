package org.sgine

/**
 * Child trait represents an object that has a parent.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Child[P] {
  def parent: () => P

  /**
   * Returns true if the value passed is in the ancestry hierarchy for this Child.
   */
  def hasAncestor[T](value: T)(implicit manifest: Manifest[T]) = ancestor((t: T) => t == value)(manifest) != None

  /**
   * Uses the supplied matching function to return the first ancestor match given the specified type or None if no
   * match is found.
   */
  def ancestor[T](matcher: T => Boolean)(implicit manifest: Manifest[T]): Option[T] = {
    val p = parent()
    if (p != null) {
      if (manifest.erasure.isAssignableFrom(p.asInstanceOf[AnyRef].getClass) && matcher(p.asInstanceOf[T])) {
        Option(p.asInstanceOf[T])
      } else if (p.isInstanceOf[Child[_]]) {
        p.asInstanceOf[Child[_]].ancestor[T](matcher)(manifest)
      } else {
        None
      }
    } else {
      None
    }
  }
}