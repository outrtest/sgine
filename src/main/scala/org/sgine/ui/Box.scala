package org.sgine.ui

import org.sgine.core.Color
import org.sgine.render.primitive.{Cuboid, Primitive}
import org.sgine.property.AdvancedProperty
import org.sgine.render.RenderImage

/*
 * A box with some variable width, height, depth, color, and texture image.
 *
 * By default will create an untextured white 100 unit large box.
 */
class Box() extends PrimitiveComponent {

  /*
   * A box with a specified width, height, depth, and optionally color and texture.
   */
  def this(width: Double, height: Double, depth: Double, color: Color = Color.White, texture: RenderImage = null) {
    this()
    this.width := width
    this.height := height
    this.depth := depth
    this.color := color
    this.texture := texture
  }

  /*
   * A box with a specified size, color and texture.
   */
  def this(size: Double, color: Color, texture: RenderImage) {
    this(size, size, size, color, texture)
  }

  /*
   * A box with a specified size and color.
   */
  def this(size: Double, color: Color) {
    this(size, color, null)
  }

  /*
   * A box with a specified size.
   */
  def this(size: Double) {
    this(size, Color.White)
  }

  /* Width of the box (size along x axis). */
  val width = new AdvancedProperty[Double](100, this, null, null, invalidationHandler)

  /* Height of the box (size along y axis). */
  val height = new AdvancedProperty[Double](100, this, null, null, invalidationHandler)

  /* depth of the box (size along z axis). */
  val depth = new AdvancedProperty[Double](100, this, null, null, invalidationHandler)

  /* Texture image of the box, or null if no texture should be used. */
  val texture = new AdvancedProperty[RenderImage](null, this, null, null, invalidationHandler)

  // Update mesh when color changes, because color info is embedded in it
  color.listeners += invalidationHandler

  protected def createPrimitive(): Primitive = {
    // TODO: Replace use of Image with Texture
    Cuboid(width(), height(), depth(), color(), texture())
  }

}