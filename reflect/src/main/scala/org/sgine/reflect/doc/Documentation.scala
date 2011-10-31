package org.sgine.reflect.doc

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class Documentation(html: String) {
  lazy val text = Documentation.stripHTML(html)
}

object Documentation {
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
    b.toString
  }
}