package org.sgine.ui

import render.ArrayBuffer
import org.sgine.property.Property

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ShapeComponent extends RenderableComponent {
  private val arrayBuffer = new ArrayBuffer(false)

  val vertices = Property[Seq[Double]](Nil)

  onUpdate(vertices) {
    arrayBuffer.data = vertices.value
  }

  protected def draw() = {
    arrayBuffer.bind()
    arrayBuffer.drawVertices()
  }
}