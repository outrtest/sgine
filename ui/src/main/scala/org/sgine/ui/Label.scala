package org.sgine.ui


/**
 * Label represents a single line of text.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Label extends TextComponent {
  /**
   * The text to be displayed.
   */
  def text = _text

  /**
   * The font the text should be displayed with.
   */
  def font = _font
}

object Label {
  /**
   * Convenience method to create a Label.
   */
  def apply(text: String) = {
    val l = new Label()
    l.text := text
    l
  }
}