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
    val p = parent()
    if (p != null) {
      if (manifest.erasure.isAssignableFrom(p.asInstanceOf[AnyRef].getClass) && matcher(p.asInstanceOf[T])) {
        Option(p.asInstanceOf[T])
      } else if (p.isInstanceOf[Child[_]] && maxDepth > 1) {
        p.asInstanceOf[Child[_]].ancestor[T](matcher, maxDepth - 1)(manifest)
      } else {
        None
      }
    } else {
      None
    }
  }
}