package org.sgine.ui

import align.{VerticalAlignment, HorizontalAlignment}
import font.BitmapFont
import org.sgine.Resource

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object AlignmentExample extends UI {
  implicit val font = BitmapFont(Resource("arial64.fnt"))

  val label1 = Label("Hello")
  label1.alignment.horizontal := HorizontalAlignment.Right
  label1.alignment.vertical := VerticalAlignment.Bottom
  contents += label1

  val label2 = Label("World")
  label2.alignment.horizontal := HorizontalAlignment.Left
  label2.alignment.vertical := VerticalAlignment.Top
  contents += label2
}
