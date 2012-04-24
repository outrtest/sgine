package org.sgine.ui

import align.{VerticalAlignment, HorizontalAlignment}
import scala.math._

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class FPSLabel(format: String = "%s fps", frequency: Double = 1.0) extends Label {
  private var entries = 0
  private var time = 0.0

  includeInLayout := false
  alignment.horizontal := HorizontalAlignment.Left
  alignment.vertical := VerticalAlignment.Top

  override def update(delta: Double) = {
    time += delta
    entries += 1
    if (time >= frequency) {
      val fps = round(entries / time)
      text := format.format(fps)
      entries = 0
      time = 0.0
    }

    super.update(delta)
  }
}
