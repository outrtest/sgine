package org.sgine.reflect.doc

/**
 * Documentation represents documentation HTML.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class Documentation(html: String) {
  lazy val text = Documentation.stripHTML(html)
}

object Documentation {
  /**
   * As the name implies this method strips HTML from the supplied String.
   */
  def stripHTML(s: String) = {
    val b = new StringBuilder
    var open = false
    for (c <- s) {
      if (c == '>' && open) {
        open = false
      } else if (open) {
        // Ignore
      } else if (c == '<') {
        open = true
      } else {
        b.append(c)
      }
    }
    b.toString()
  }
}