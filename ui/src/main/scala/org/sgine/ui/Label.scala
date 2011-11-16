package org.sgine.ui

import font.BitmapFont

/**
 * Label represents a single line of text.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Label extends TextComponent {
  def text = _text
  def font = _font
}

object Label {
  def apply(text: String)(implicit font: BitmapFont) = {
    val l = new Label()
    l.text := text
    l.font := font
    l
  }
}